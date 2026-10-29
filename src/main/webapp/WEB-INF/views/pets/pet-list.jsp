<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Find a pet - StrayToStay</title>
    <link rel="stylesheet" href="<c:url value='/static/css/main.css'/>">
</head>
<body>
<main class="container">
    <header class="page-header">
        <a href="<c:url value='/'/>" class="brand">StrayToStay</a>
        <h1>Find a pet</h1>
    </header>

    <form method="get" action="<c:url value='/pets'/>" class="filters">
        <label>Species
            <select name="species">
                <option value="">Any</option>
                <c:forEach var="s" items="${species}">
                    <option value="${s}" ${criteria.species == s ? 'selected' : ''}><c:out value="${fn:toLowerCase(s)}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Size
            <select name="size">
                <option value="">Any</option>
                <c:forEach var="s" items="${sizes}">
                    <option value="${s}" ${criteria.size == s ? 'selected' : ''}><c:out value="${fn:toLowerCase(s)}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Breed
            <input type="text" name="breed" maxlength="80" value="<c:out value='${criteria.breed}'/>">
        </label>
        <label>City
            <input type="text" name="city" maxlength="80" value="<c:out value='${criteria.city}'/>">
        </label>
        <label>Max age (months)
            <input type="number" name="maxAgeMonths" min="0" max="360" value="<c:out value='${criteria.maxAgeMonths}'/>">
        </label>
        <label>
            <input type="checkbox" name="goodWithKids" value="true" ${criteria.goodWithKids ? 'checked' : ''}>
            Good with kids
        </label>
        <button type="submit">Search</button>
        <a href="<c:url value='/pets'/>" class="link">Clear</a>
    </form>

    <p class="result-count">
        <c:choose>
            <c:when test="${page.total == 0}">No pets found. Try fewer filters.</c:when>
            <c:when test="${page.total == 1}">1 pet found</c:when>
            <c:otherwise>${page.total} pets found</c:otherwise>
        </c:choose>
    </p>

    <section class="pet-grid">
        <c:forEach var="pet" items="${page.items}">
            <article class="pet-card">
                <h2><a href="<c:url value='/pet'><c:param name='id' value='${pet.id}'/></c:url>"><c:out value="${pet.name}"/></a></h2>
                <p class="meta">
                    <c:out value="${fn:toLowerCase(pet.species)}"/>
                    <c:if test="${not empty pet.breed}"> &middot; <c:out value="${pet.breed}"/></c:if>
                    <c:if test="${not empty pet.size}"> &middot; <c:out value="${fn:toLowerCase(pet.size)}"/></c:if>
                </p>
                <p class="meta"><c:out value="${pet.ageDisplay}"/> &middot; <c:out value="${fn:toLowerCase(pet.sex)}"/></p>
                <p class="tags">
                    <c:if test="${pet.vaccinated}"><span class="tag">vaccinated</span></c:if>
                    <c:if test="${pet.neutered}"><span class="tag">neutered</span></c:if>
                    <c:if test="${pet.goodWithKids}"><span class="tag">good with kids</span></c:if>
                    <c:if test="${pet.goodWithPets}"><span class="tag">good with pets</span></c:if>
                    <span class="tag tag-type"><c:out value="${fn:toLowerCase(pet.listingType)}"/></span>
                </p>
            </article>
        </c:forEach>
    </section>

    <c:if test="${page.totalPages > 1}">
        <nav class="pager">
            <c:url var="prevUrl" value="/pets">
                <c:param name="species" value="${criteria.species}"/>
                <c:param name="size" value="${criteria.size}"/>
                <c:param name="breed" value="${criteria.breed}"/>
                <c:param name="city" value="${criteria.city}"/>
                <c:param name="maxAgeMonths" value="${criteria.maxAgeMonths}"/>
                <c:param name="goodWithKids" value="${criteria.goodWithKids}"/>
                <c:param name="page" value="${page.page - 1}"/>
            </c:url>
            <c:url var="nextUrl" value="/pets">
                <c:param name="species" value="${criteria.species}"/>
                <c:param name="size" value="${criteria.size}"/>
                <c:param name="breed" value="${criteria.breed}"/>
                <c:param name="city" value="${criteria.city}"/>
                <c:param name="maxAgeMonths" value="${criteria.maxAgeMonths}"/>
                <c:param name="goodWithKids" value="${criteria.goodWithKids}"/>
                <c:param name="page" value="${page.page + 1}"/>
            </c:url>
            <c:if test="${page.hasPrevious}"><a href="${prevUrl}">&laquo; Previous</a></c:if>
            <span>Page ${page.page} of ${page.totalPages}</span>
            <c:if test="${page.hasNext}"><a href="${nextUrl}">Next &raquo;</a></c:if>
        </nav>
    </c:if>
</main>
<script src="<c:url value='/static/js/main.js'/>"></script>
</body>
</html>
