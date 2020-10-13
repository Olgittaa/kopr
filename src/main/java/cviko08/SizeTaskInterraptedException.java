package cviko08;

public class SizeTaskInterraptedException extends RuntimeException{
    private DirSize dirSize;

    public SizeTaskInterraptedException(DirSize dirSize) {
        this.dirSize = dirSize;
    }

    public SizeTaskInterraptedException(Throwable cause, DirSize dirSize) {
        super(cause);
        this.dirSize = dirSize;
    }

    public DirSize getDirSize() {
        return dirSize;
    }
}
