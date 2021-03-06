package org.petka.pis.persistence.restquery;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestQueryFilter {

    public static final String SEPARATOR_CHARS = ",";
    private static final String PAGE = "page";
    private static final String SIZE = "size";
    private static final String SORT = "sort";
    private static final String FILTER = "filter";
    public static final int MAX_PAGE_SIZE = 1000;
    public static final int DEFAULT_PAGE_SIZE = 50;


    private final HttpServletRequest request;


    @Autowired
    public RequestQueryFilter(final HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Get Query created from the request parameters.
     *
     * @return {@link Query}
     */
    public Query getRequestQuery() {
        Query query = new Query();

        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.entrySet().forEach(e -> convertParamToCriteria(query, e));
        handlePagingParam(query, parameterMap);
        handleSortParam(query, parameterMap);

        return query;
    }

    private void convertParamToCriteria(final Query query, final Map.Entry<String, String[]> param) {
        String paramName = param.getKey();
        String[] paramValue = param.getValue();
        if (ArrayUtils.isNotEmpty(paramValue)) {
            if (!StringUtils.equalsAnyIgnoreCase(paramName, PAGE, SIZE, SORT, FILTER)) {
                handleParam(query, paramName, paramValue[0]);
            }
            if (FILTER.equals(paramName)) {
                handleFilterParam(query, paramValue);
            }
        }
    }

    private void handleParam(final Query query, final String param, final String value) {
        String symbolCandidate = StringUtils.substringBefore(value, SEPARATOR_CHARS);
        SearchCriteria searchCriteria = SearchCriteria.builder().key(param)
                .operation(SearchOperation.getSimpleOperation(symbolCandidate.charAt(0)))
                .value(value).build();
        query.add(searchCriteria);
    }

    private void handleFilterParam(final Query query, final String[] values) {
        for (String value : values) {
            String[] queryParam = StringUtils.split(value, SEPARATOR_CHARS);
            SearchCriteria criteria = SearchCriteria.builder().key(queryParam[0])
                    .operation(SearchOperation.getSimpleOperation(queryParam[1].charAt(0)))
                    .value(queryParam[1].substring(1)).build();
            query.add(criteria);
        }
    }

    private void handlePagingParam(final Query query, final Map<String, String[]> parameterMap) {
        int page = 0;
        int size = DEFAULT_PAGE_SIZE;
        String[] pageParam = parameterMap.get(PAGE);
        if (pageParam != null && pageParam.length > 0) {
            page = Integer.parseInt(pageParam[0]);
        }

        String[] sizeParam = parameterMap.get(SIZE);
        if (sizeParam != null && sizeParam.length > 0) {
            size = Math.min(Integer.parseInt(sizeParam[0]), MAX_PAGE_SIZE);
        }
        query.paginate(page, size);
    }

    private void handleSortParam(final Query query, final Map<String, String[]> parameterMap) {
        String[] sortParam = parameterMap.get(SORT);
        if (sortParam != null && sortParam.length > 0) {
            String sort = sortParam[0];
            //TODO handle direction and multiple fields
            Sort.Order order = new Sort.Order(Sort.Direction.ASC, sort);
            query.add(order);
        }
    }
}
