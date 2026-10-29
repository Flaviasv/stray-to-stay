<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${pet.name}"/> - StrayToStay</title>
    <link rel="stylesheet" href="<c:url value='/static/css/main.css'/>">
</head>
<body>
<main class="container">
    <header class="page-header">
        <a href="<c:url value='/'/>" class="brand">StrayToStay</a>
        <a href="<c:url value='/pets'/>" class="link">&laquo; Back to search</a>
    </header>

    <article class="pet-detail">
        <h1><c:out value="${pet.name}"/></h1>
        <p class="meta">
            <c:out value="${fn:toLowerCase(pet.species)}"/>
            <c:if test="${not empty pet.breed}"> &middot; <c:out value="${pet.breed}"/></c:if>
            &middot; <c:out value="${pet.ageDisplay}"/>
            &middot; <c:out value="${fn:toLowerCase(pet.sex)}"/>
            <c:if test="${not empty pet.size}"> &middot; <c:out value="${fn:toLowerCase(pet.size)}"/></c:if>
        </p>
        <p class="tags">
            <span class="tag tag-type">for <c:out value="${fn:toLowerCase(pet.listingType)}"/></span>
            <c:if test="${pet.vaccinated}"><span class="tag">vaccinated</span></c:if>
            <c:if test="${pet.neutered}"><span class="tag">neutered</span></c:if>
            <c:if test="${pet.goodWithKids}"><span class="tag">good with kids</span></c:if>
            <c:if test="${pet.goodWithPets}"><span class="tag">good with pets</span></c:if>
        </p>

        <c:if test="${not empty pet.description}">
            <h2>About <c:out value="${pet.name}"/></h2>
            <p><c:out value="${pet.description}"/></p>
        </c:if>
        <c:if test="${not empty pet.medicalNotes}">
            <h2>Medical notes</h2>
            <p><c:out value="${pet.medicalNotes}"/></p>
        </c:if>

        <c:if test="${not empty shelter}">
            <h2>Shelter</h2>
            <p>
                <strong><c:out value="${shelter.name}"/></strong><br>
                <c:out value="${shelter.city}"/><c:if test="${not empty shelter.province}">, <c:out value="${shelter.province}"/></c:if><br>
                <c:if test="${not empty shelter.email}"><c:out value="${shelter.email}"/><br></c:if>
                <c:if test="${not empty shelter.phone}"><c:out value="${shelter.phone}"/></c:if>
            </p>
        </c:if>

        <p class="actions">
            <%-- Inquiry form is added in the next sprint; it will POST to /inquiry --%>
            <button type="button" disabled title="Coming in the next sprint">Send an inquiry</button>
        </p>
    </article>
</main>
<script src="<c:url value='/static/js/main.js'/>"></script>
</body>
</html>
