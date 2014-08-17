/**
 * A class for printing objects to a
 * Toast message.
 */
public class ObjectPrinter {

    @NotNull
    private final Context mContext;

    @NotNull
    private Object mObject;

    public ObjectPrinter(@NotNull Context context, @NotNull Object object) {
        mContext = context;
        mObject = object;
    }

    public void setObject(@NotNull Object object) {
        mObject = object;
    }

    private void print() {
        Toast.makeText(mContext, mObject.toString(), Toast.LENGTH_LONG).show();
    }
}