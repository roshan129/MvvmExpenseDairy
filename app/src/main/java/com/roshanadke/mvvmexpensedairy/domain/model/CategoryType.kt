package com.roshanadke.mvvmexpensedairy.domain.model

enum class CategoryType(val displayName: String) {
    OTHERS("Others"),
    FOOD_AND_DINING("Food and Dining"),
    SHOPPING("Shopping"),
    TRAVELLING("Travelling"),
    ENTERTAINMENT("Entertainment"),
    MEDICAL("Medical"),
    PERSONAL_CARE("Personal Care"),
    EDUCATION("Education"),
    BILLS_AND_UTILITIES("Bills and Utilities"),
    INVESTMENTS("Investments"),
    RENT("Rent"),
    TAXES("Taxes"),
    INSURANCE("Insurance"),
    GIFTS_AND_DONATIONS("Gifts and Donations");

    fun getAllDisplayNames(): List<String> {
        return values().map { it.displayName }
    }

    fun getCategoryType(type: String) {
        CategoryType.OTHERS.name
    }
}


