package AshleyIngram.FYP.Nephele;

import eu.stratosphere.types.Value;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

public class StringArrayView implements Value {
    private static final long serialVersionUID = 1L;

    private ArrayList<String> entries = new ArrayList<String>();

    private int numEntries = 0;

    public StringArrayView() {
    }

    public String get(int index) {
        if (index >= numEntries) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return getQuick(index);
    }

    public String getQuick(int index) {
        return entries.get(index);
    }

    public void allocate(int numEntries) {
        this.numEntries = numEntries;
    }

    public void add(String value) {

    }

    public void set(int index, String value) {
        if (index >= numEntries) {
            throw new ArrayIndexOutOfBoundsException();
        }
        setQuick(index, value);
    }

    public void setQuick(int index, String value) {
        entries.set(index, value);
    }

    public int size() {
        return numEntries;
    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(numEntries);
        for (int n = 0; n < numEntries; n++) {
            out.writeUTF(entries.get(n));
        }
    }

    public void read(DataInput in) throws IOException {
        numEntries = in.readInt();
        for (int n = 0; n < numEntries; n++) {
            entries.set(n, in.readUTF());
        }
    }
}
