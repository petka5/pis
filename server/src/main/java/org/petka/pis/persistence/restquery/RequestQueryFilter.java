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

    private static final String SEPARATOR_CHARS = ",";
    private static final String PAGE = "page";
    private static final String SIZE = "size";
    private static final String SORT = "sort";
    private static final String FILTER = "filter";
    private static final String INCLUDE_DELETED = "includeDeleted";
    private static final int MAX_PAGE_SIZE = 1000;
    private static final int DEFAULT_PAGE_SIZE = 50;


    private final HttpServletRequest request;


    @Autowired
    public RequestQueryFilter(final HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Get Query created from the request parameters.
     *
     * @return {@link RestQuery}
     */
    public RestQuery getRequestQuery() {
        RestQuery restQuery = new RestQuery();

        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.entrySet().forEach(e -> convertParamToCriteria(restQuery, e));
        handlePagingParam(restQuery, parameterMap);
        handleSortParam(restQuery, parameterMap);

        return restQuery;
    }

    private void convertParamToCriteria(final RestQuery restQuery, final Map.Entry<String, String[]> param) {
        String paramName = param.getKey();
        String[] paramValue = param.getValue();
        if (ArrayUtils.isNotEmpty(paramValue)) {
            if (!StringUtils.equalsAnyIgnoreCase(paramName, PAGE, SIZE, SORT, FILTER, INCLUDE_DELETED)) {
                handleParam(restQuery, paramName, paramValue[0]);
            }
            if (FILTER.equals(paramName)) {
                handleFilterParam(restQuery, paramValue);
            }
        }
    }

    private void handleParam(final RestQuery restQuery, final String param, final String value) {
        String symbolCandidate = StringUtils.substringBefore(value, SEPARATOR_CHARS);
        SearchCriteria searchCriteria = SearchCriteria.builder().key(param)
                .operation(SearchOperation.getSimpleOperation(symbolCandidate.charAt(0)))
                .value(value).build();
        restQuery.add(searchCriteria);
    }

    private void handleFilterParam(final RestQuery restQuery, final String[] values) {
        for (String value : values) {
            String[] queryParam = StringUtils.split(value, SEPARATOR_CHARS);
            SearchCriteria criteria = SearchCriteria.builder().key(queryParam[0])
                    .operation(SearchOperation.getSimpleOperation(queryParam[1].charAt(0)))
                    .value(queryParam[1].substring(1)).build();
            restQuery.add(criteria);
        }
    }

    private void handlePagingParam(final RestQuery restQuery, final Map<String, String[]> parameterMap) {
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
        restQuery.paginate(page, size);
    }

    private void handleSortParam(final RestQuery restQuery, final Map<String, String[]> parameterMap) {
        String[] sortParam = parameterMap.get(SORT);
        if (sortParam != null && sortParam.length > 0) {
            String sort = sortParam[0];
            //TODO handle direction and multiple fields
            Sort.Order order = new Sort.Order(Sort.Direction.ASC, sort);
            restQuery.add(order);
        }
    }
}
