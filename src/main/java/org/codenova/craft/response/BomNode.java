package org.codenova.craft.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class BomNode {
    private String id;
    private String label;
    private List<BomNode> children;
}
