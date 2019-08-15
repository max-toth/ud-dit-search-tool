
package ru.taskdata.dto;

import java.util.List;
import lombok.Builder;
import lombok.Singular;

@Builder
public class FindGroupBObjType {
    private final String logicOper;
    @Singular("findCondBObj")
    private final List<FindCondBObjType> findCondBObj;

}
