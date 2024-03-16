package edu.cuhk.csci3310.ui.utils

/*
    The status of an item that is being fetched from the database
    The UI can use these states to update itself
*/
enum class FetchStatus {
    FETCHING,
    FINISHED,
    NOT_EXISTS,
    ERROR,
}
