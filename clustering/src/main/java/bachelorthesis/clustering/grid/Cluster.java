package bachelorthesis.clustering.grid;

import java.util.LinkedHashSet;
import java.util.Set;

public class Cluster {

    private Set<Cell> clusterCells;
    private Set<Cluster> neighbors;
    private Set<Cluster> parts;
    private String name;

    public Cluster() {

        clusterCells = new LinkedHashSet<>();
        neighbors = new LinkedHashSet<>();
        parts = new LinkedHashSet<>();
        parts.add(this);
    }

    public Cluster(Set<Cell> clusterCells, Set<Cluster> neighbors) {

        setClusterCells(clusterCells);
        setNeighbors(neighbors);
        parts = new LinkedHashSet<>();
        parts.add(this);
    }

    public Cluster(Cell clusterCell, Set<Cluster> neighbors) {

        clusterCells = new LinkedHashSet<>();
        clusterCells.add(clusterCell);
        setNeighbors(neighbors);
        parts = new LinkedHashSet<>();
        parts.add(this);
    }

    public Set<Cell> getClusterCells() {
        return clusterCells;
    }

    public void setClusterCells(Set<Cell> clusterCells) {
        this.clusterCells = clusterCells;
    }

    public Set<Cluster> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Set<Cluster> neighbors) {
        this.neighbors = neighbors;
    }

    public Set<Cluster> getParts() {
        return parts;
    }

    public void setParts(Set<Cluster> parts) {
        this.parts = parts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private boolean removeNeighbor(Cluster neighbor) {

        return neighbors.remove(neighbor);
    }

    private boolean removeNeighbors(Set<Cluster> neighbor) {

        return neighbors.removeAll(neighbor);
    }

    private boolean addNeighbors(Set<Cluster> neighbors) {

        return this.neighbors.addAll(neighbors);
    }

    public boolean addNeighbor(Cluster neighbor) {

        return neighbors.add(neighbor);
    }

    private boolean addClusterCells(Cluster cluster) {

        return clusterCells.addAll(cluster.getClusterCells());
    }

    private boolean addParts(Cluster cluster) {

        return parts.addAll(cluster.getParts());
    }

    public void mergeClusters(Cluster merger) {

        /*System.out.println("Neighbors before merging:");
        System.out.println("Cluster:  " + getName());
        printNeighbors();
        System.out.println("Merger:   " + merger.getName());
        merger.printNeighbors();*/

        merger.removeNeighbors(getParts());
        //System.out.println(getName() + " " + getParts() + "  " + this.getParts());
        /*for (Cluster c : getParts()) {
            System.out.println("parts: " + c.getName());
        }*/
        removeNeighbors(merger.getParts());

        merger.addNeighbors(neighbors);
        addNeighbors(merger.getNeighbors());

        merger.addParts(this);
        addParts(merger);

        //System.out.println("merger, neighbors: " + merger.getNeighbors().size());
        //System.out.println("neighbors: " + getNeighbors().size());

        addClusterCells(merger);
        merger.addClusterCells(this);   // is this really necessary

        /*System.out.println("Neighbors after merging:");
        System.out.println("Cluster:  " + getName());
        printNeighbors();
        System.out.println("Merger:   " + merger.getName());
        merger.printNeighbors();*/
    }

    private void printNeighbors() {

        for (Cluster neighbor : neighbors) {
            System.out.println(neighbor.getName() + " parts:");
            for (Cluster part : neighbor.getParts()) {
                System.out.print("    " + part.getName());
            }
            System.out.println();
        }
    }
}
