package nn.neural;

import nn.model.Node;
import nn.model.Type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class NodeProxier {

    private Map<Node, AnnNode> matchNodes = new HashMap<>();

    public AnnNode convert(Node node){
        return matchNodes.computeIfAbsent(node, this::convert2);
    }

    private AnnNode convert2(Node node) {
        return new AnnNode(convertType(node.getType()), node.getInnovation(), node.getBias());
    }

    private AnnType convertType(Type type) {
        AnnType result;
         switch(type) {
            case SENSOR: result = AnnType.INPUT; break;
            case HIDDEN: result = AnnType.HIDDEN; break;
            case OUTPUT: result = AnnType.OUTPUT; break;
            default: throw new RuntimeException();
        }
        return result;
    }

    public Collection<AnnNode> getAllNodes() {
        return matchNodes.values();
    }
}
