/**
 * A class for printing objects to a
 * Toast message.
 */
public class ObjectPrinter {

    private final Context mContext;

    private Object mObject;

    public ObjectPrinter(Context context,
                         Object object) {
        mContext = context;
        mObject = object;
    }

    public void setObject(Object object) {
        mObject = object;
    }

    private void print() {
        Toast.makeText(
                mContext,
                mObject.toString(),
                Toast.LENGTH_LONG
        ).show();
    }
}