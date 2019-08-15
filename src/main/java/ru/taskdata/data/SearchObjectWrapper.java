package ru.taskdata.data;

import lombok.Builder;
import lombok.Getter;
import ru.taskdata.dto.ProfileDataSearchRequestBObjType;

@Getter
@Builder
public class SearchObjectWrapper {
    private final long row;
    private final ProfileDataSearchRequestBObjType object;
}
