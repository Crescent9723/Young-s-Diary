package data;

/**
 * Created by Justin on 9/7/2015.
 */
public class EventIconData {
    private String name;
    private int resourceID;

    public EventIconData(String name, int resourceID) {
        this.name = name;
        this.resourceID = resourceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }
}
