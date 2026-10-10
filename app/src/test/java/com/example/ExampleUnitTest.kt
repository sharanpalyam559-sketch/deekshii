package com.example

import com.example.data.DeekshiiRepository
import com.example.data.model.SubjectRecord
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testAttendancePercentageCalculation() {
        val subject = SubjectRecord(
            id = "test-1",
            code = "CS601",
            name = "Machine Learning",
            faculty = "Dr. Sunitha",
            credits = 4,
            attendedClasses = 30,
            totalClasses = 40,
            mid1Marks = 25.0,
            mid2Marks = 28.0,
            internalMarks = 36.0,
            targetMarks = 90.0,
            syllabusProgress = 0.8f,
            accentColorHex = 0xFF917BBF
        )

        assertEquals(75.0f, subject.attendancePercentage, 0.01f)
        assertTrue(subject.isSafeAttendance)
    }

    @Test
    fun testBunkCalculatorThreshold() {
        val lowAttendanceSubject = SubjectRecord(
            id = "test-2",
            code = "CS605",
            name = "Software Engineering",
            faculty = "Prof. Rajesh",
            credits = 3,
            attendedClasses = 20,
            totalClasses = 30, // 66.6% < 75%
            mid1Marks = 20.0,
            mid2Marks = 22.0,
            internalMarks = 30.0,
            targetMarks = 85.0,
            syllabusProgress = 0.7f,
            accentColorHex = 0xFFF6A97A
        )

        assertFalse(lowAttendanceSubject.isSafeAttendance)
        assertTrue(lowAttendanceSubject.bunkStatusText.contains("Attend next"))
    }

    @Test
    fun testRepositoryInitialData() {
        val repo = DeekshiiRepository()
        assertEquals("Deekshitha", repo.profile.value.name)
        assertTrue(repo.subjects.value.isNotEmpty())
        assertTrue(repo.dailyTasks.value.isNotEmpty())
        assertTrue(repo.examDeadlines.value.isNotEmpty())
        assertTrue(repo.forumPosts.value.isNotEmpty())
    }
}
