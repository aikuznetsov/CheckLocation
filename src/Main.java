import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class Main {

        public static void main(String[] args) throws IOException {


            File f = new File("/Users/a.kuznetsov/Downloads/test/data.csv");
            BufferedReader fin = new BufferedReader(new FileReader(f));

            int plusrez =0, minusRez =0, notCountry = 0;
            String line;
            while ((line = fin.readLine()) != null) {
                try
                {
                    Thread.sleep(500);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
                String [] rawLine = line.split(",");
                String simCountry = rawLine[1];
                String [] editLine = rawLine[4].split("source = ");
                String [] editLineMass = editLine[1].split(",");
                String lineLat = editLineMass[0];
                String lineLon = rawLine[5];
                String Url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lineLat + "," + lineLon + "&sensor=true&key=AIzaSyCVk_FspUrr1tfg86Se1QXGBSMOAJz4swE";
                String response = "";

                URL yahoo = new URL(Url);
                URLConnection yc = yahoo.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                yc.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null)
                    response = response + inputLine;
                in.close();

                boolean countryExist = response.contains("\"formatted_address\" :");
                boolean countrZero = response.contains("ZERO_RESULTS");

                if (countryExist != true ) {
                    if (countrZero) {
                        try {
                            String filename = "/Users/a.kuznetsov/Downloads/test/GeoData.txt";
                            FileWriter fw = new FileWriter(filename, true);
                            fw.write(simCountry + " , " + lineLat + " , " + lineLon + " , " + "Google API doesn't provide country \n");
                            fw.close();
                            notCountry++;
                        } catch (IOException ioe) {
                            System.err.println("IOException: " + ioe.getMessage());
                        }
                    }else{
                        System.out.println("Google API doesn't exist!");
                        System.out.println(response);
                        break;
                    }

                }else {
                    String[] countryPart = response.split("\"formatted_address\" :");

                    String[] country = countryPart[0].split("\"");

                    if (countryPart.length >= 8) {
                        country = countryPart[7].split("\"");
                    } else if (countryPart.length >= 7) {
                        country = countryPart[6].split("\"");
                    } else if (countryPart.length >= 6) {
                        country = countryPart[5].split("\"");
                    } else if (countryPart.length >= 5) {
                        country = countryPart[4].split("\"");
                    } else if (countryPart.length >= 4) {
                        country = countryPart[3].split("\"");
                    } else if (countryPart.length >= 3) {
                        country = countryPart[2].split("\"");
                    } else if (countryPart.length >= 2) {
                        country = countryPart[1].split("\"");
                    } else {
                        country = countryPart[0].split("\"");
                        ;
                    }


                    boolean result = response.contains(simCountry);
                    if (result) plusrez++;
                    else minusRez++;

                    String resultSt = String.valueOf(result);

                    System.out.println(resultSt);

                    try {
                        String filename = "/Users/a.kuznetsov/Downloads/test/GeoData.txt";
                        FileWriter fw = new FileWriter(filename, true);
                        fw.write(simCountry + " , " + lineLat + " , " + lineLon + " , " + country[1] + " , " + resultSt + "\n");
                        fw.close();
                    } catch (IOException ioe) {
                        System.err.println("IOException: " + ioe.getMessage());
                    }
                }
            }


            String filename= "/Users/a.kuznetsov/Downloads/test/GeoData.txt";
            FileWriter fw = new FileWriter(filename,true);
            fw.write("Total:" + "\n");
            fw.write("Correct: " + Integer.toString(plusrez) + "\n");
            fw.write("Incorrect: " + Integer.toString(minusRez) + "\n");
            fw.write("Google API doesn't provide country: " + Integer.toString(notCountry) + "\n");
            fw.close();


            System.out.println("Total:");
            System.out.println("Correct: " + Integer.toString(plusrez));
            System.out.println("Incorrect: " + Integer.toString(minusRez));
            System.out.println("Google API doesn't provide country: " + Integer.toString(notCountry));
        }

}
