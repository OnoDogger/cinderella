package org.ono.domain;

/**
 * Created by ono on 2018/11/19.
 */
public class FileLocation {

    private String file;
    private String protocal;

    public FileLocation() {
    }

    public FileLocation(String file, String protocal) {
        this.file = file;
        this.protocal = protocal;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileLocation that = (FileLocation) o;

        if (file != null ? !file.equals(that.file) : that.file != null) return false;
        return protocal != null ? protocal.equals(that.protocal) : that.protocal == null;
    }

    @Override
    public int hashCode() {
        int result = file != null ? file.hashCode() : 0;
        result = 31 * result + (protocal != null ? protocal.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FileLocation{" +
                "file='" + file + '\'' +
                ", protocal='" + protocal + '\'' +
                '}';
    }
}
