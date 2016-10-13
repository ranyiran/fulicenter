package cn.ran.flicenter.bean;

/**
 * Created by Administrator on 2016/10/13.
 */
public class AlbumsBean {
    private int pid;

    private int imgId;

    private String imgUrl;

    private String thumbUrl;

    public void setPid(int pid){
        this.pid = pid;
    }
    public int getPid(){
        return this.pid;
    }
    public void setImgId(int imgId){
        this.imgId = imgId;
    }
    public int getImgId(){
        return this.imgId;
    }
    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }
    public String getImgUrl(){
        return this.imgUrl;
    }
    public void setThumbUrl(String thumbUrl){
        this.thumbUrl = thumbUrl;
    }
    public String getThumbUrl(){
        return this.thumbUrl;
    }

    @Override
    public String toString() {
        return "AlbumsBean{" +
                "pid=" + pid +
                ", imgId=" + imgId +
                ", imgUrl='" + imgUrl + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                '}';
    }

}
