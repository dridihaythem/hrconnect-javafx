package utils;

import models.Candidat;

public class SessionManager {
    private static Candidat candidat;
    private static boolean isRH;

    public static void setCandidat(Candidat candidat) {
        SessionManager.candidat = candidat;
    }

    public static Candidat getCandidat() {
        return candidat;
    }

    public static void setIsRH(boolean isRH) {
        SessionManager.isRH = isRH;
    }

    public static boolean getIsRH() {
        return isRH;
    }
}