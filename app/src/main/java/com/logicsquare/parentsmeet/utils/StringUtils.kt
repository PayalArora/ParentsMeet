package com.logicsquare.parentsmeet.utils

fun ifContainsDigit(string: String): Boolean {
    if (string.contains("[0-9]".toRegex())) {
        return true
    }
    return false
}

fun ifContainsUpperCase(string: String): Boolean {
    if (string.contains("[A-Z]".toRegex())) {
        return true
    }
    return false
}

fun ifContainsLowerCase(string: String): Boolean {
    if (string.contains("[a-z]".toRegex())) {
        return true
    }
    return false
}

fun ifContainsSpecialChar(string: String): Boolean {
    if (string.contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())) {
        return true
    }
    return false
}