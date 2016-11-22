package generalassembly.yuliyakaleda.solution_code.model;

import java.util.List;

/**
 * Created by charlie on 11/21/16.
 */

public class WalmartRootObject {
    private List<WalmartItem> items;

    public void setItems(List<WalmartItem> items){
        this.items = items;
    }

    public List<WalmartItem> getItems(){
        return items;
    }
}
