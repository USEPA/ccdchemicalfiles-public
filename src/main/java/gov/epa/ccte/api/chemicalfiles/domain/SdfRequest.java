package gov.epa.ccte.api.chemicalfiles.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SdfRequest {
    String dtxsid;
    Double similarity;
}
