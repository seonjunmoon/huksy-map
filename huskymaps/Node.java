package huskymaps;

import autocomplete.Term;
import huskymaps.params.Location;

/** Vertex representation for the graph. */
class Node extends Location implements Term {
    final long id;
    final int importance;

    Node(long id, double lat, double lon, String name, int importance) {
        super(lat, lon, name);
        this.id = id;
        this.importance = importance;
    }

    long id() {
        return id;
    }

    int importance() {
        return importance;
    }

    @Override
    public String query() {
        return name;
    }

    @Override
    public String queryPrefix(int r) {
        return name.substring(0, Math.min(r, name.length()));
    }

    @Override
    public long weight() {
        return importance;
    }

    static class Builder {
        long id;
        double lat;
        double lon;
        String name;
        int importance;

        Builder setId(long id) {
            this.id = id;
            return this;
        }

        Builder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        Builder setLon(double lon) {
            this.lon = lon;
            return this;
        }

        Builder setName(String name) {
            this.name = name;
            return this;
        }

        Builder setImportance(int importance) {
            this.importance = importance;
            return this;
        }

        Node createNode() {
            return new Node(id, lat, lon, name, importance);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Node otherNode = (Node) other;
        return this.id == otherNode.id;
    }

    @Override
    public int hashCode() {
        return (int) this.id;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", name='" + name + '\'' +
                ", importance=" + importance +
                '}';
    }
}
