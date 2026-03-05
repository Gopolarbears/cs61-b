import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private static final int DEPTHS = 8;

    private double[] depthLongDpp;

    public Rasterer() {
        // YOUR CODE HERE
        depthLongDpp = new double[DEPTHS];
        for (int i = 0; i < depthLongDpp.length; i++) {
            depthLongDpp[i] = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / (MapServer.TILE_SIZE * Math.pow(2, i));
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        if (!validateParams(params)) {
            results.put("query_success", false);
            return results;
        }
        determineDepth(params, results);
        findImages(params, results);
        results.put("query_success", true);
        return results;
    }

    private boolean validateParams(Map<String, Double> params) {
        return params.get("lrlon") > MapServer.ROOT_ULLON
               && params.get("ullon") < MapServer.ROOT_LRLON
               && params.get("lrlat") < MapServer.ROOT_ULLAT
               && params.get("ullat") > MapServer.ROOT_LRLAT
               && (params.get("lrlon") > params.get("ullon"))
               && (params.get("ullat") > params.get("lrlat"));
    }

    private void determineDepth(Map<String, Double> params, Map<String, Object> results) {
        double londDpp = (params.get("lrlon") - params.get("ullon")) / params.get("w");
        for (int i = 0; i < depthLongDpp.length; i++) {
            if (depthLongDpp[i] < londDpp) {
                results.put("depth", i);
                return;
            }
        }
        results.put("depth", depthLongDpp.length - 1);
    }

    private void findImages(Map<String, Double> params, Map<String, Object> results) {
        double lonPerTile = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, (int) results.get("depth"));
        double latPerTile = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, (int) results.get("depth"));
        int leftIndex = (int) ((params.get("ullon") - MapServer.ROOT_ULLON) / lonPerTile);
        int rightIndex = (int) Math.pow(2, (int) results.get("depth")) - 1
                - (int) ((MapServer.ROOT_LRLON - params.get("lrlon")) / lonPerTile);
        int upperIndex = (int) ((MapServer.ROOT_ULLAT - params.get("ullat")) / latPerTile);
        int lowerIndex = (int) Math.pow(2, (int) results.get("depth")) - 1
                - (int) ((params.get("lrlat") - MapServer.ROOT_LRLAT) / latPerTile);
        leftIndex = Math.max(leftIndex, 0);
        rightIndex = Math.min(rightIndex, (int) Math.pow(2, (int) results.get("depth")) - 1);
        upperIndex = Math.max(upperIndex, 0);
        lowerIndex = Math.min(lowerIndex, (int) Math.pow(2, (int) results.get("depth")) - 1);

        // Corner Cases
        results.put("raster_ul_lon", MapServer.ROOT_ULLON + leftIndex * lonPerTile);
        results.put("raster_lr_lon", MapServer.ROOT_ULLON + (rightIndex + 1) * lonPerTile);
        results.put("raster_ul_lat", MapServer.ROOT_ULLAT - upperIndex * latPerTile);
        results.put("raster_lr_lat", MapServer.ROOT_ULLAT - (lowerIndex + 1) * latPerTile);

        String[][] renderGrid = new String[lowerIndex - upperIndex + 1][rightIndex - leftIndex + 1];

        for (int i = 0; i < lowerIndex - upperIndex + 1; i++) {
            for (int j = 0; j < rightIndex - leftIndex + 1; j++) {
                renderGrid[i][j] = "d"
                        + String.valueOf(results.get("depth"))
                        + "_x"
                        + (leftIndex + j)
                        + "_y"
                        + (upperIndex + i)
                        + ".png";
            }
        }
        results.put("render_grid", renderGrid);
    }

}
