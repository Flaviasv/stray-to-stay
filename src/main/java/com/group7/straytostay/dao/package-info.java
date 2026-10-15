/**
 * Data access objects. One interface per aggregate (PetDao, UserDao, ShelterDao, InquiryDao) with a PostgreSQL implementation that uses PreparedStatement only. DaoFactory creates them and is the seam for mocking in tests.
 */
package com.group7.straytostay.dao;
