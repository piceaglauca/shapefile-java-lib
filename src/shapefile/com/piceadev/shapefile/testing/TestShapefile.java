import com.piceadev.shapefile;

public class TestShapefile {
    public static void main (String[] args) {

        Shapefile expectedData = new Shapefile ();
        Shapefile dataFromFile = new Shapefile ();

        populateTestData (expectedData);
        readDataFromFile (dataFromFile);

        if (assertDataMatches (expectedData, dataFromFile)) {
            System.out.println ("Data matches");
        } else {
            System.out.println ("Data did not match");
        }
    }

    private static void populateTestData (Shapefile data) {
        data
    }
}
