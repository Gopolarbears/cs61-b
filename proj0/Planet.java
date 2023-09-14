public class Planet{
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    String imgFileName;
    static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV,
    double yV, double m, String img){
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p){
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet P){
        double distance = Math.sqrt((xxPos-P.xxPos)*(xxPos-P.xxPos) + (yyPos-P.yyPos)*(yyPos-P.yyPos));
        return distance;
    }

    public double calcForceExertedBy(Planet P){
        double force = (G * mass * P.mass) / (calcDistance(P) * calcDistance(P));
        return force; 
    }

    public double calcForceExertedByX(Planet P){
        double force = calcForceExertedBy(P);
        double x_force = force * (P.xxPos-xxPos) / calcDistance(P);
        return x_force;
    }

    public double calcForceExertedByY(Planet P){
        double force = calcForceExertedBy(P);
        double y_force = force * (P.yyPos-yyPos) / calcDistance(P);
        return y_force;
    }

    public double calcNetForceExertedByX(Planet[] allPlanets){
        double x_netforce = 0;
        for (Planet x: allPlanets){
            if (!this.equals(x)){
                x_netforce += calcForceExertedByX(x);
            }
        }
        return x_netforce;
    }

    public double calcNetForceExertedByY(Planet[] allPlanets){
        double y_netforce = 0;
        for (Planet x: allPlanets){
            if (!this.equals(x)){
                y_netforce += calcForceExertedByY(x);
            }
        }
        return y_netforce;
    }

    public void update(double time, double xxFor, double yyFor){
        double xxAcc = xxFor / mass;
        double yyAcc = yyFor / mass;
        xxVel += xxAcc * time;
        yyVel += yyAcc * time;
        xxPos += time * xxVel;
        yyPos += time * yyVel;
    }

    public void draw(){
        String filename = "images/" + imgFileName;
        StdDraw.picture(xxPos, yyPos, filename);
        StdDraw.show();
    }
}