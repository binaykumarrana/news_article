package binay.inshortapp.model;

import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCallback;
import co.uk.rushorm.core.RushCore;

/**
 * Created by Binay on 08/09/17.
 */

public class Article implements Rush {
    public String TITLE;
    public int ID;
    public String URL;
    public String PUBLISHER;
    public String CATEGORY;
    public String HOSTNAME;
    public long TIMESTAMP;
    public boolean isFavArticle;

    @Override
    public void save() {
        RushCore.getInstance().save(this);
    }

    @Override
    public void save(RushCallback callback) {
        RushCore.getInstance().save(this, callback);
    }

    @Override
    public void delete() {
        RushCore.getInstance().delete(this);
    }

    @Override
    public void delete(RushCallback callback) {
        RushCore.getInstance().delete(this, callback);
    }

    @Override
    public String getId() {
        return RushCore.getInstance().getId(this);
    }

    @Override
    public String toString() {
        return "Article{" +
                "TITLE='" + TITLE + '\'' +
                ", ID=" + ID +
                ", URL='" + URL + '\'' +
                ", PUBLISHER='" + PUBLISHER + '\'' +
                ", CATEGORY='" + CATEGORY + '\'' +
                ", HOSTNAME='" + HOSTNAME + '\'' +
                ", TIMESTAMP=" + TIMESTAMP +
                '}';
    }
}
