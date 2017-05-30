package ninhpham.simpletodo;

import java.util.Date;

/**
 * Created by ninhp on 5/24/2017.
 */

public class todoItem {
    public String itemContent;
    public String dueDate;
    public String priority;

    public todoItem(String priority, String itemContent, String dueDate) {
        this.itemContent = itemContent;
        this.dueDate = dueDate;
        this.priority = priority;
    }
}
