//Kar65 //KarenRalda //November25th2025 
package Project.Common;

public class PointsPayload extends Payload {

    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return String.format(
                "PointsPayload[%s] Client Id [%s] Points: [%d]",
                getPayloadType(),
                getClientId(),
                points
        );
    }
}