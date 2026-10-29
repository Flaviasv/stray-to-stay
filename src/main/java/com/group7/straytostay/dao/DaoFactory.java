package com.group7.straytostay.dao;

/**
 * Creates the DAO implementations. Tests can replace them with mocks through the
 * set methods; production code never calls those.
 */
public final class DaoFactory {

    private static volatile PetDao petDao;
    private static volatile ShelterDao shelterDao;

    private DaoFactory() { }

    public static PetDao petDao() {
        if (petDao == null) {
            petDao = new PetDaoImpl();
        }
        return petDao;
    }

    public static ShelterDao shelterDao() {
        if (shelterDao == null) {
            shelterDao = new ShelterDaoImpl();
        }
        return shelterDao;
    }

    public static void setPetDao(PetDao dao) {
        petDao = dao;
    }

    public static void setShelterDao(ShelterDao dao) {
        shelterDao = dao;
    }

    public static void reset() {
        petDao = null;
        shelterDao = null;
    }
}
