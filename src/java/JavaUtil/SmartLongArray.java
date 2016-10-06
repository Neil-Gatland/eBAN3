package JavaUtil;

public class SmartLongArray

{
    int sp = 0; // "stack pointer" to keep track of position in the array
    private long[] array;
    private int growthSize;

    public SmartLongArray()
    {
        this( 1024 );
    }

    public SmartLongArray( int initialSize )
    {
        this( initialSize, (int)( initialSize / 4 ) );
    }

    public SmartLongArray( int initialSize, int growthSize )
    {
        this.growthSize = growthSize;
        array = new long[ initialSize ];
    }

    public void add( long i )
    {
        if( sp >= array.length ) // time to grow!
        {
            long[] tmpArray = new long[ array.length + growthSize ];
            System.arraycopy( array, 0, tmpArray, 0, array.length );
            array = tmpArray;
        }
        array[ sp ] = i;
        sp += 1;
    }

    public long[] toArray()
    {
        long[] trimmedArray = new long[ sp ];
        System.arraycopy( array, 0, trimmedArray, 0, trimmedArray.length );
        return trimmedArray;
    }
}
