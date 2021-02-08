package fr.ups;

import java.sql.Blob;

public class NotesHelper {

    int nid;
    int tid;
    String type;
    String text;
    Blob blob;

    public NotesHelper(int nid, int tid, String type, String text, Blob blob) {
        this.nid = nid;
        this.tid = tid;
        this.type = type;
        this.text = text;
        this.blob = blob;
    }

    public NotesHelper() {

    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Blob getBlob() {
        return blob;
    }

    public void setBlob(Blob blob) {
        this.blob = blob;
    }
}
