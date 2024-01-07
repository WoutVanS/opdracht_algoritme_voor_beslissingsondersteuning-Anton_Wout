public interface Constants {
     int V = 2; // V is number of vehicles
    int S = 8; // V is number of stacks
     int Cb = 1; // Cs is max capacity of the bufferpoint

     String CSV_FILE = "instructions.csv"; // Replace with your CSV file path
     String CSV_DELIMITER = ";"; // Set the delimiter used in your CSV file

    Integer BUFFER_POINT_ID = 0;

    enum statusVehicle {
        AVAILABLE,
        LOADING,
        UNLOADING,
        MOVING,
        MOVINGTOPICKUP,
        BUSSYWAITINGFORDROPOFF,
        BUSSYWAITINGFORPICKUP
    }

    enum statusRequest {
        WAITING,
        INPROGRESS,
        DONE,

        REALOCATIONINPROGRESS
    }


}
