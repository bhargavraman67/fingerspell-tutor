package com.fingerspell.tutor.data.sample

import com.fingerspell.tutor.R
import com.fingerspell.tutor.domain.model.FingerState.BENT
import com.fingerspell.tutor.domain.model.FingerState.CURLED
import com.fingerspell.tutor.domain.model.FingerState.EXTENDED
import com.fingerspell.tutor.domain.model.FingerState.TUCKED
import com.fingerspell.tutor.domain.model.Handshape
import com.fingerspell.tutor.domain.model.HandshapeSpec
import com.fingerspell.tutor.domain.model.Lesson
import com.fingerspell.tutor.domain.model.SignEntry
import com.fingerspell.tutor.domain.model.SignMotion
import com.fingerspell.tutor.domain.model.SignTopic
import com.fingerspell.tutor.domain.model.ThumbPose
import com.fingerspell.tutor.domain.model.WordEntry
import com.fingerspell.tutor.domain.model.WordGroup

object SampleData {

    val openPalm = HandshapeSpec(EXTENDED, EXTENDED, EXTENDED, EXTENDED, ThumbPose.OUT, 0f, 0.9f)
    val flatHand = HandshapeSpec(EXTENDED, EXTENDED, EXTENDED, EXTENDED, ThumbPose.ACROSS, 0f, 0.1f)
    val fistHand = HandshapeSpec(CURLED, CURLED, CURLED, CURLED, ThumbPose.ACROSS, 0f, 0f)
    val pointHand = HandshapeSpec(EXTENDED, TUCKED, TUCKED, TUCKED, ThumbPose.ACROSS, 0f, 0f)
    val clawHand = HandshapeSpec(BENT, BENT, BENT, BENT, ThumbPose.OUT, 0f, 0.6f)
    val pinchHand = HandshapeSpec(BENT, BENT, BENT, BENT, ThumbPose.UP, 0f, 0.15f)

    val handshapes: List<Handshape> = listOf(
        Handshape("A", HandshapeSpec(CURLED, CURLED, CURLED, CURLED, ThumbPose.SIDE, 0f, 0.05f),
            listOf(R.string.tip_a_1, R.string.tip_a_2, R.string.tip_a_3), listOf("S", "T")),
        Handshape("B", HandshapeSpec(EXTENDED, EXTENDED, EXTENDED, EXTENDED, ThumbPose.ACROSS, 0f, 0.05f),
            listOf(R.string.tip_b_1, R.string.tip_b_2, R.string.tip_b_3), listOf("F")),
        Handshape("C", HandshapeSpec(BENT, BENT, BENT, BENT, ThumbPose.OUT, -12f, 0.2f),
            listOf(R.string.tip_c_1, R.string.tip_c_2, R.string.tip_c_3), listOf("O")),
        Handshape("D", HandshapeSpec(EXTENDED, CURLED, CURLED, CURLED, ThumbPose.ACROSS, 0f, 0.1f),
            listOf(R.string.tip_d_1, R.string.tip_d_2, R.string.tip_d_3), listOf("F")),
        Handshape("E", HandshapeSpec(CURLED, CURLED, CURLED, CURLED, ThumbPose.TUCKED, 0f, 0.05f),
            listOf(R.string.tip_e_1, R.string.tip_e_2, R.string.tip_e_3), listOf("O")),
        Handshape("F", HandshapeSpec(CURLED, EXTENDED, EXTENDED, EXTENDED, ThumbPose.ACROSS, 0f, 0.3f),
            listOf(R.string.tip_f_1, R.string.tip_f_2, R.string.tip_f_3), listOf("D")),
        Handshape("G", HandshapeSpec(EXTENDED, TUCKED, TUCKED, TUCKED, ThumbPose.SIDE, -72f, 0f),
            listOf(R.string.tip_g_1, R.string.tip_g_2, R.string.tip_g_3), listOf("Q")),
        Handshape("H", HandshapeSpec(EXTENDED, EXTENDED, TUCKED, TUCKED, ThumbPose.TUCKED, -72f, 0f),
            listOf(R.string.tip_h_1, R.string.tip_h_2, R.string.tip_h_3), listOf("U")),
        Handshape("I", HandshapeSpec(TUCKED, TUCKED, TUCKED, EXTENDED, ThumbPose.ACROSS, 0f, 0f),
            listOf(R.string.tip_i_1, R.string.tip_i_2, R.string.tip_i_3), listOf("J")),
        Handshape("J", HandshapeSpec(TUCKED, TUCKED, TUCKED, EXTENDED, ThumbPose.ACROSS, 18f, 0f),
            listOf(R.string.tip_j_1, R.string.tip_j_2, R.string.tip_j_3), listOf("I")),
        Handshape("K", HandshapeSpec(EXTENDED, EXTENDED, TUCKED, TUCKED, ThumbPose.UP, 0f, 0.55f),
            listOf(R.string.tip_k_1, R.string.tip_k_2, R.string.tip_k_3), listOf("P")),
        Handshape("L", HandshapeSpec(EXTENDED, TUCKED, TUCKED, TUCKED, ThumbPose.OUT, 0f, 0f),
            listOf(R.string.tip_l_1, R.string.tip_l_2, R.string.tip_l_3), listOf("D")),
        Handshape("M", HandshapeSpec(BENT, BENT, BENT, TUCKED, ThumbPose.TUCKED, 0f, 0.05f),
            listOf(R.string.tip_m_1, R.string.tip_m_2, R.string.tip_m_3), listOf("N")),
        Handshape("N", HandshapeSpec(BENT, BENT, TUCKED, TUCKED, ThumbPose.TUCKED, 0f, 0.05f),
            listOf(R.string.tip_n_1, R.string.tip_n_2, R.string.tip_n_3), listOf("M")),
        Handshape("O", HandshapeSpec(BENT, BENT, BENT, BENT, ThumbPose.UP, 0f, 0.05f),
            listOf(R.string.tip_o_1, R.string.tip_o_2, R.string.tip_o_3), listOf("E", "C")),
        Handshape("P", HandshapeSpec(EXTENDED, EXTENDED, TUCKED, TUCKED, ThumbPose.UP, 162f, 0.55f),
            listOf(R.string.tip_p_1, R.string.tip_p_2, R.string.tip_p_3), listOf("K")),
        Handshape("Q", HandshapeSpec(EXTENDED, TUCKED, TUCKED, TUCKED, ThumbPose.SIDE, 162f, 0f),
            listOf(R.string.tip_q_1, R.string.tip_q_2, R.string.tip_q_3), listOf("G")),
        Handshape("R", HandshapeSpec(EXTENDED, EXTENDED, TUCKED, TUCKED, ThumbPose.ACROSS, 0f, 0f),
            listOf(R.string.tip_r_1, R.string.tip_r_2, R.string.tip_r_3), listOf("U", "V")),
        Handshape("S", HandshapeSpec(CURLED, CURLED, CURLED, CURLED, ThumbPose.ACROSS, 0f, 0f),
            listOf(R.string.tip_s_1, R.string.tip_s_2, R.string.tip_s_3), listOf("A", "T")),
        Handshape("T", HandshapeSpec(CURLED, CURLED, CURLED, CURLED, ThumbPose.TUCKED, 0f, 0f),
            listOf(R.string.tip_t_1, R.string.tip_t_2, R.string.tip_t_3), listOf("A", "S")),
        Handshape("U", HandshapeSpec(EXTENDED, EXTENDED, TUCKED, TUCKED, ThumbPose.ACROSS, 0f, 0.08f),
            listOf(R.string.tip_u_1, R.string.tip_u_2, R.string.tip_u_3), listOf("V", "R")),
        Handshape("V", HandshapeSpec(EXTENDED, EXTENDED, TUCKED, TUCKED, ThumbPose.ACROSS, 0f, 1f),
            listOf(R.string.tip_v_1, R.string.tip_v_2, R.string.tip_v_3), listOf("U", "R")),
        Handshape("W", HandshapeSpec(EXTENDED, EXTENDED, EXTENDED, TUCKED, ThumbPose.ACROSS, 0f, 0.85f),
            listOf(R.string.tip_w_1, R.string.tip_w_2, R.string.tip_w_3), listOf("V")),
        Handshape("X", HandshapeSpec(BENT, TUCKED, TUCKED, TUCKED, ThumbPose.ACROSS, 0f, 0f),
            listOf(R.string.tip_x_1, R.string.tip_x_2, R.string.tip_x_3), listOf("D")),
        Handshape("Y", HandshapeSpec(TUCKED, TUCKED, TUCKED, EXTENDED, ThumbPose.OUT, 0f, 0f),
            listOf(R.string.tip_y_1, R.string.tip_y_2, R.string.tip_y_3), listOf("I")),
        Handshape("Z", HandshapeSpec(EXTENDED, TUCKED, TUCKED, TUCKED, ThumbPose.ACROSS, 12f, 0f),
            listOf(R.string.tip_z_1, R.string.tip_z_2, R.string.tip_z_3), listOf("D"))
    )

    val lessons: List<Lesson> = listOf(
        Lesson(1, listOf("A", "B", "C"), 1),
        Lesson(2, listOf("D", "E", "F"), 1),
        Lesson(3, listOf("G", "H", "I"), 1),
        Lesson(4, listOf("J", "K", "L"), 1),
        Lesson(5, listOf("M", "N", "O"), 1),
        Lesson(6, listOf("P", "Q", "R"), 1),
        Lesson(7, listOf("S", "T", "U"), 1),
        Lesson(8, listOf("V", "W", "X"), 1),
        Lesson(9, listOf("Y", "Z"), 1),
        Lesson(10, emptyList(), 2),
        Lesson(11, emptyList(), 2),
        Lesson(12, emptyList(), 2),
        Lesson(13, emptyList(), 2),
        Lesson(14, emptyList(), 2),
        Lesson(15, emptyList(), 2),
        Lesson(16, emptyList(), 2),
        Lesson(17, emptyList(), 2)
    )

    val topics: List<SignTopic> = listOf(
        SignTopic(1, R.string.topic_greetings),
        SignTopic(2, R.string.topic_family),
        SignTopic(3, R.string.topic_food),
        SignTopic(4, R.string.topic_feelings),
        SignTopic(5, R.string.topic_time),
        SignTopic(6, R.string.topic_places),
        SignTopic(7, R.string.topic_school),
        SignTopic(8, R.string.topic_courtesy)
    )

    val wordGroups: List<WordGroup> = listOf(
        WordGroup(1, R.string.word_group_three),
        WordGroup(2, R.string.word_group_four),
        WordGroup(3, R.string.word_group_names)
    )

    val words: List<WordEntry> = listOf(
        WordEntry("CAT", 1), WordEntry("DOG", 1), WordEntry("SUN", 1), WordEntry("HAT", 1),
        WordEntry("CUP", 1), WordEntry("BED", 1), WordEntry("MAP", 1), WordEntry("PEN", 1),
        WordEntry("HAND", 2), WordEntry("BOOK", 2), WordEntry("MILK", 2), WordEntry("STAR", 2),
        WordEntry("FISH", 2), WordEntry("TREE", 2), WordEntry("RAIN", 2), WordEntry("GIFT", 2),
        WordEntry("ANNA", 3), WordEntry("LUIS", 3), WordEntry("MAYA", 3), WordEntry("OMAR", 3),
        WordEntry("ROSA", 3), WordEntry("NOAH", 3), WordEntry("IVAN", 3), WordEntry("ZARA", 3)
    )

    val signs: List<SignEntry> = listOf(
        SignEntry(1, R.string.sign_hello, R.string.sign_hello_desc, 1, SignMotion.ARC_OUT, flatHand, searchKey = "HELLO"),
        SignEntry(2, R.string.sign_goodbye, R.string.sign_goodbye_desc, 1, SignMotion.WAVE, openPalm, searchKey = "GOODBYE"),
        SignEntry(3, R.string.sign_thank_you, R.string.sign_thank_you_desc, 1, SignMotion.FORWARD, flatHand, searchKey = "THANK YOU"),
        SignEntry(4, R.string.sign_please, R.string.sign_please_desc, 1, SignMotion.CIRCLE, flatHand, searchKey = "PLEASE"),
        SignEntry(5, R.string.sign_sorry, R.string.sign_sorry_desc, 1, SignMotion.CIRCLE, fistHand, searchKey = "SORRY"),
        SignEntry(6, R.string.sign_meet, R.string.sign_meet_desc, 1, SignMotion.FORWARD, pointHand, searchKey = "NICE TO MEET YOU"),
        SignEntry(7, R.string.sign_mother, R.string.sign_mother_desc, 2, SignMotion.TAP_CHIN, openPalm, searchKey = "MOTHER"),
        SignEntry(8, R.string.sign_father, R.string.sign_father_desc, 2, SignMotion.UP_DOWN, openPalm, searchKey = "FATHER"),
        SignEntry(9, R.string.sign_sister, R.string.sign_sister_desc, 2, SignMotion.ARC_OUT, pointHand, searchKey = "SISTER"),
        SignEntry(10, R.string.sign_brother, R.string.sign_brother_desc, 2, SignMotion.ARC_OUT, pointHand, searchKey = "BROTHER"),
        SignEntry(11, R.string.sign_baby, R.string.sign_baby_desc, 2, SignMotion.WAVE, flatHand, searchKey = "BABY"),
        SignEntry(12, R.string.sign_family, R.string.sign_family_desc, 2, SignMotion.CIRCLE, pinchHand, searchKey = "FAMILY"),
        SignEntry(13, R.string.sign_eat, R.string.sign_eat_desc, 3, SignMotion.TAP_CHIN, pinchHand, searchKey = "EAT"),
        SignEntry(14, R.string.sign_drink, R.string.sign_drink_desc, 3, SignMotion.TAP_CHIN, clawHand, searchKey = "DRINK"),
        SignEntry(15, R.string.sign_water, R.string.sign_water_desc, 3, SignMotion.TAP_CHIN, openPalm, searchKey = "WATER"),
        SignEntry(16, R.string.sign_bread, R.string.sign_bread_desc, 3, SignMotion.UP_DOWN, flatHand, searchKey = "BREAD"),
        SignEntry(17, R.string.sign_apple, R.string.sign_apple_desc, 3, SignMotion.CIRCLE, fistHand, searchKey = "APPLE"),
        SignEntry(18, R.string.sign_milk, R.string.sign_milk_desc, 3, SignMotion.UP_DOWN, fistHand, searchKey = "MILK"),
        SignEntry(19, R.string.sign_happy, R.string.sign_happy_desc, 4, SignMotion.CIRCLE, flatHand, searchKey = "HAPPY"),
        SignEntry(20, R.string.sign_sad, R.string.sign_sad_desc, 4, SignMotion.UP_DOWN, openPalm, searchKey = "SAD"),
        SignEntry(21, R.string.sign_tired, R.string.sign_tired_desc, 4, SignMotion.UP_DOWN, clawHand, searchKey = "TIRED"),
        SignEntry(22, R.string.sign_excited, R.string.sign_excited_desc, 4, SignMotion.CIRCLE, clawHand, searchKey = "EXCITED"),
        SignEntry(23, R.string.sign_angry, R.string.sign_angry_desc, 4, SignMotion.ARC_OUT, clawHand, searchKey = "ANGRY"),
        SignEntry(24, R.string.sign_calm, R.string.sign_calm_desc, 4, SignMotion.UP_DOWN, flatHand, searchKey = "CALM"),
        SignEntry(25, R.string.sign_now, R.string.sign_now_desc, 5, SignMotion.UP_DOWN, clawHand, searchKey = "NOW"),
        SignEntry(26, R.string.sign_later, R.string.sign_later_desc, 5, SignMotion.FORWARD, pointHand, searchKey = "LATER"),
        SignEntry(27, R.string.sign_today, R.string.sign_today_desc, 5, SignMotion.UP_DOWN, clawHand, searchKey = "TODAY"),
        SignEntry(28, R.string.sign_tomorrow, R.string.sign_tomorrow_desc, 5, SignMotion.ARC_OUT, fistHand, searchKey = "TOMORROW"),
        SignEntry(29, R.string.sign_morning, R.string.sign_morning_desc, 5, SignMotion.ARC_OUT, flatHand, searchKey = "MORNING"),
        SignEntry(30, R.string.sign_night, R.string.sign_night_desc, 5, SignMotion.ARC_OUT, openPalm, searchKey = "NIGHT"),
        SignEntry(31, R.string.sign_home, R.string.sign_home_desc, 6, SignMotion.TAP_CHIN, pinchHand, searchKey = "HOME"),
        SignEntry(32, R.string.sign_school, R.string.sign_school_desc, 6, SignMotion.UP_DOWN, flatHand, searchKey = "SCHOOL"),
        SignEntry(33, R.string.sign_store, R.string.sign_store_desc, 6, SignMotion.FORWARD, pinchHand, searchKey = "STORE"),
        SignEntry(34, R.string.sign_park, R.string.sign_park_desc, 6, SignMotion.UP_DOWN, openPalm, searchKey = "PARK"),
        SignEntry(35, R.string.sign_city, R.string.sign_city_desc, 6, SignMotion.ARC_OUT, flatHand, searchKey = "CITY"),
        SignEntry(36, R.string.sign_outside, R.string.sign_outside_desc, 6, SignMotion.ARC_OUT, pinchHand, searchKey = "OUTSIDE"),
        SignEntry(37, R.string.sign_learn, R.string.sign_learn_desc, 7, SignMotion.ARC_OUT, clawHand, searchKey = "LEARN"),
        SignEntry(38, R.string.sign_teacher, R.string.sign_teacher_desc, 7, SignMotion.FORWARD, pinchHand, searchKey = "TEACHER"),
        SignEntry(39, R.string.sign_book, R.string.sign_book_desc, 7, SignMotion.WAVE, flatHand, searchKey = "BOOK"),
        SignEntry(40, R.string.sign_question, R.string.sign_question_desc, 7, SignMotion.CIRCLE, pointHand, searchKey = "QUESTION"),
        SignEntry(41, R.string.sign_practice, R.string.sign_practice_desc, 7, SignMotion.UP_DOWN, fistHand, searchKey = "PRACTICE"),
        SignEntry(42, R.string.sign_finish, R.string.sign_finish_desc, 7, SignMotion.WAVE, openPalm, searchKey = "FINISH"),
        SignEntry(43, R.string.sign_yes, R.string.sign_yes_desc, 8, SignMotion.UP_DOWN, fistHand, searchKey = "YES"),
        SignEntry(44, R.string.sign_no, R.string.sign_no_desc, 8, SignMotion.FORWARD, pointHand, searchKey = "NO"),
        SignEntry(45, R.string.sign_help, R.string.sign_help_desc, 8, SignMotion.UP_DOWN, fistHand, searchKey = "HELP"),
        SignEntry(46, R.string.sign_wait, R.string.sign_wait_desc, 8, SignMotion.WAVE, openPalm, searchKey = "WAIT"),
        SignEntry(47, R.string.sign_understand, R.string.sign_understand_desc, 8, SignMotion.UP_DOWN, pointHand, searchKey = "UNDERSTAND"),
        SignEntry(48, R.string.sign_repeat, R.string.sign_repeat_desc, 8, SignMotion.ARC_OUT, clawHand, searchKey = "REPEAT")
    )
}
