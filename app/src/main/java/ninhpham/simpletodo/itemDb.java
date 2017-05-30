package ninhpham.simpletodo;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

import static android.R.attr.name;
import static com.raizlabs.android.dbflow.config.FlowLog.Level.I;

/**
 * Created by ninhp on 5/19/2017.
 */

@Table(database = MyDatabase.class)
public class itemDb extends BaseModel {
    @Column
    @PrimaryKey
    private int id;

    @Column
    private String itemDb;

    @Column
    private String dueDate;

    @Column
    private String priority;

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemDb() {
        return itemDb;
    }

    public void setItemDb(String itemDb) {
        this.itemDb = itemDb;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}