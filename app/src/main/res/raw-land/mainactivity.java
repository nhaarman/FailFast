private static final String MESSAGE = "Hello, world!";

private Object mObject;

private ObjectPrinter mObjectPrinter;

@Override
protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mObjectPrinter = new ObjectPrinter(this, MESSAGE);
}

private void print(){
    mObjectPrinter.print();
}

private void doStuff() {
    /* Bunch of other code */

    /* The next line introduces a bug,
       which can be hard to find: */
    mObjectPrinter.setObject(mObject);

    /* Bunch of other code */
}