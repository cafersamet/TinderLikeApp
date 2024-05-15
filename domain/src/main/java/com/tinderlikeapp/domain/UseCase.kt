package com.tinderlikeapp.domain

interface UseCase<in P: UseCaseParam?, out R: UseCaseResult> {
    fun execute(param: P): suspend () -> R
}

interface UseCaseParam {
    companion object NoParam: UseCaseParam
}

interface UseCaseResult {
    companion object NoResult: UseCaseResult
}