package huskymaps;

class ContractableNode extends Node {
    private static final int UNCONTRACTED = Integer.MIN_VALUE;

    private int contractionOrder;
    private int depth;

    ContractableNode(long id, double lat, double lon, String name, int importance) {
        super(id, lat, lon, name, importance);
        contractionOrder = UNCONTRACTED;
        depth = 1;
    }

    boolean isContracted() {
        return contractionOrder != UNCONTRACTED;
    }

    int contractionOrder() {
        return contractionOrder;
    }

    void setContractionOrder(int order) {
        contractionOrder = order;
    }

    int getDepth() {
        return depth;
    }

    void updateDepths(Iterable<ContractableNode> neighbors) {
        for (ContractableNode neighbor : neighbors) {
            if (!neighbor.isContracted()) {
                neighbor.depth = Math.max(neighbor.depth, this.depth + 1);
            }
        }
    }

    static class Builder extends Node.Builder {
        ContractableNode createNode() {
            return new ContractableNode(id, lat, lon, name, importance);
        }
    }

    @Override
    public String toString() {
        return "ContractableNode{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", name='" + name + '\'' +
                ", importance=" + importance +
                ", contractionOrder=" + contractionOrder +
                ", depth=" + depth +
                '}';
    }
}
