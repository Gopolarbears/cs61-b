public class NBody {
    public static double readRadius(String filename) {
        In in = new In(filename);
        int number = in.readInt();
        double RadiusInFile = in.readDouble();
        return RadiusInFile;
    }

    public static Planet[] readPlanets(String filename) {
        In in = new In(filename);
        int number = in.readInt();
        double RadiusInFile = in.readDouble();
        Planet[] array_of_Planets = new Planet[number];
        for(int i=0; i<number; i++){
            array_of_Planets[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
        }
        return array_of_Planets;
    }

    public static void main(String[] args){
        StdDraw.enableDoubleBuffering();
  
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Planet[] all_Planets = readPlanets(filename);
        double radius = readRadius(filename);

        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");
        StdDraw.show();

        for (Planet x: all_Planets){
            x.draw();
        }

        double current_time = 0;
        int number = all_Planets.length;
        while(current_time < T){
            double[] xForces = new double[number];
            double[] yForces = new double[number];
            for (int i = 0; i < number; i++){
                xForces[i] = all_Planets[i].calcNetForceExertedByX(all_Planets);
                yForces[i] = all_Planets[i].calcNetForceExertedByY(all_Planets);
            }
            for (int i = 0; i < number; i++){
                all_Planets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (Planet x: all_Planets) {
                x.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            current_time += dt;
        }

    StdOut.printf("%d\n", all_Planets.length);
    StdOut.printf("%.2e\n", radius);
    for (int i = 0; i < all_Planets.length; i++) {
        StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    all_Planets[i].xxPos, all_Planets[i].yyPos, all_Planets[i].xxVel,
                    all_Planets[i].yyVel, all_Planets[i].mass, all_Planets[i].imgFileName);   
        }   
    }
}
