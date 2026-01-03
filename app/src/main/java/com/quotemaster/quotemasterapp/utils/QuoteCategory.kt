package com.quotemaster.quotemasterapp.utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class QuoteCategory(
    val displayName: String,
    val backgroundGradient: Brush,
    val imageDescription: String,
    val imageRes: Int
)
{
    INSPIRATIONAL(
        "Inspirational",
        Brush.linearGradient(colors = listOf(Color(0xFF1e3c72), Color(0xFF2a5298))),
        "Mountain landscape at dawn",
        imageRes = Icons.inspiration
    ),
    MOTIVATIONAL(
        "Motivational",
        Brush.linearGradient(colors = listOf(Color(0xFF654ea3), Color(0xFFeaafc8))),
        "Rocky mountain path",
        imageRes = Icons.motivation
    ),
    LOVE(
        "Love",
        Brush.linearGradient(colors = listOf(Color(0xFFc94b4b), Color(0xFF4b134f))),
        "Heart-shaped decorations",
        imageRes = Icons.love
    ),
    FRIENDSHIP(
        "Friendship",
        Brush.linearGradient(colors = listOf(Color(0xFF8360c3), Color(0xFF2ebf91))),
        "Children playing together",
        imageRes = Icons.friendship
    ),
    HAPPINESS(
        "Happiness",
        Brush.linearGradient(colors = listOf(Color(0xFF00b4db), Color(0xFF0083b0))),
        "People celebrating",
        imageRes = Icons.happiness
    ),
    LIFE(
        "Life",
        Brush.linearGradient(colors = listOf(Color(0xFF8b5cf6), Color(0xFF06b6d4))),
        "Person with umbrella",
        imageRes = Icons.life
    ),
    POSITIVE(
        "Positive",
        Brush.linearGradient(colors = listOf(Color(0xFF38ef7d), Color(0xFF11998e))),
        "Hands raised in victory",
        imageRes = Icons.positive
    ),
    SUCCESS(
        "Success",
        Brush.linearGradient(colors = listOf(Color(0xFF667eea), Color(0xFF764ba2))),
        "Mountain peak achievement",
        imageRes = Icons.success
    ),
    FAMILY(
        "Family",
        Brush.linearGradient(colors = listOf(Color(0xFFf093fb), Color(0xFFf5576c))),
        "Family portrait",
        imageRes = Icons.family
    ),
    AGE(
        "Age",
        Brush.linearGradient(colors = listOf(Color(0xFFff9a9e), Color(0xFFfad0c4))),
        "Clock representing passage of time",
        imageRes = Icons.age
    ),
    ALONE(
        "Alone",
        Brush.linearGradient(colors = listOf(Color(0xFF4e54c8), Color(0xFF8f94fb))),
        "Person sitting alone",
        imageRes = Icons.politics
    ),
    AMAZING(
        "Amazing",
        Brush.linearGradient(colors = listOf(Color(0xFF43cea2), Color(0xFF185a9d))),
        "Fireworks in the sky",
        imageRes = Icons.famous
    ),
    ANGER(
        "Anger",
        Brush.linearGradient(colors = listOf(Color(0xFFff6a00), Color(0xFFee0979))),
        "Stormy clouds or angry face",
        imageRes = Icons.sad
    ),
    ANNIVERSARY(
        "Anniversary",
        Brush.linearGradient(colors = listOf(Color(0xFFfdfbfb), Color(0xFFebedee))),
        "Anniversary celebration with cake",
        imageRes = Icons.marriage
    ),
    ARCHITECTURE(
        "Architecture",
        Brush.linearGradient(colors = listOf(Color(0xFF3e5151), Color(0xFFdecba4))),
        "Modern building or skyline",
        imageRes = Icons.design
    ),
    ART(
        "Art",
        Brush.linearGradient(colors = listOf(Color(0xFFc6ffdd), Color(0xFFfbd786), Color(0xFFf7797d))),
        "Paint palette or brushes",
        imageRes = Icons.art
    ),
    ATTITUDE(
        "Attitude",
        Brush.linearGradient(colors = listOf(Color(0xFFff9966), Color(0xFFff5e62))),
        "Confident person with sunglasses",
        imageRes = Icons.men
    ),
    BEAUTY(
        "Beauty",
        Brush.linearGradient(colors = listOf(Color(0xFFffecd2), Color(0xFFfcb69f))),
        "Beautiful scenery or flowers",
        imageRes = Icons.women
    ),
    BEST(
        "Best",
        Brush.linearGradient(colors = listOf(Color(0xFF00c3ff), Color(0xFFffff1c))),
        "Gold medal or trophy",
        imageRes = Icons.famous
    ),
    BIRTHDAY(
        "Birthday",
        Brush.linearGradient(colors = listOf(Color(0xFFffdde1), Color(0xFFee9ca7))),
        "Birthday cake with candles",
        imageRes = Icons.birthday
    ),
    BUSINESS(
        "Business",
        Brush.linearGradient(colors = listOf(Color(0xFFbdc3c7), Color(0xFF2c3e50))),
        "Office setup or briefcase",
        imageRes = Icons.courage
    ),
    CAR(
        "Car",
        Brush.linearGradient(colors = listOf(Color(0xFFd9a7c7), Color(0xFFfffcdc))),
        "Luxury or vintage car",
        imageRes = Icons.car
    ),
    CHANGE(
        "Change",
        Brush.linearGradient(colors = listOf(Color(0xFFde6161), Color(0xFF2657eb))),
        "Butterfly or transformation symbol",
        imageRes = Icons.education
    ),
    CHRISTMAS(
        "Christmas",
        Brush.linearGradient(colors = listOf(Color(0xFFff0844), Color(0xFFffb199))),
        "Christmas tree or ornaments",
        imageRes = Icons.christmas
    ),
    COMMUNICATION(
        "Communication",
        Brush.linearGradient(colors = listOf(Color(0xFF56ab2f), Color(0xFFa8e063))),
        "Chat bubble or people talking",
        imageRes = Icons.communication
    ),
    WISDOM(
        "Wisdom",
        Brush.linearGradient(colors = listOf(Color(0xFF01E255), Color(0xFF09FC06))),
        "Wisdom themed image",
        imageRes = Icons.wisdom
    ),
    FUNNY(
        "Funny",
        Brush.linearGradient(colors = listOf(Color(0xFF01E256), Color(0xFF09FC07))),
        "Funny themed image",
        imageRes = Icons.funny
    ),

    HUMOR(
        "Humor",
        Brush.linearGradient(colors = listOf(Color(0xFF01E257), Color(0xFF09FC08))),
        "Humor themed image",
        imageRes = Icons.humor
    ),
    SMILE(
        "Smile",
        Brush.linearGradient(colors = listOf(Color(0xFF01E258), Color(0xFF09FC09))),
        "Smile themed image",
        imageRes = Icons.smile
    ),
    COMPUTERS(
        "Computers",
        Brush.linearGradient(colors = listOf(Color(0xFF01E25A), Color(0xFF09FC0B))),
        "Computers themed image",
        imageRes = Icons.computers
    ),
    COURAGE(
        "Courage",
        Brush.linearGradient(colors = listOf(Color(0xFF01E25B), Color(0xFF09FC0C))),
        "Courage themed image",
        imageRes = Icons.courage
    ),

    DAD(
        "Dad",
        Brush.linearGradient(colors = listOf(Color(0xFF01E25C), Color(0xFF09FC0D))),
        "Dad themed image",
        imageRes = Icons.dad
    ),

    DATING(
        "Dating",
        Brush.linearGradient(colors = listOf(Color(0xFF01E25D), Color(0xFF09FC0E))),
        "Dating themed image",
        imageRes = Icons.dating
    ),

    EASTER(
        "Easter",
        Brush.linearGradient(colors = listOf(Color(0xFF01E25E), Color(0xFF09FC0F))),
        "Easter themed image",
        imageRes = Icons.easter
    ),

    EDUCATION(
        "Education",
        Brush.linearGradient(colors = listOf(Color(0xFF01E25F), Color(0xFF09FC10))),
        "Education themed image",
        imageRes = Icons.education
    ),

    ENVIRONMENTAL(
        "Environmental",
        Brush.linearGradient(colors = listOf(Color(0xFF01E260), Color(0xFF09FC11))),
        "Environmental themed image",
        imageRes = Icons.environmental
    ),

    EQUALITY(
        "Equality",
        Brush.linearGradient(colors = listOf(Color(0xFF01E261), Color(0xFF09FC12))),
        "Equality themed image",
        imageRes = Icons.equality
    ),

    EXPERIENCE(
        "Experience",
        Brush.linearGradient(colors = listOf(Color(0xFF01E262), Color(0xFF09FC13))),
        "Experience themed image",
        imageRes = Icons.experience
    ),

    FAILURE(
        "Failure",
        Brush.linearGradient(colors = listOf(Color(0xFF01E263), Color(0xFF09FC14))),
        "Failure themed image",
        imageRes = Icons.failure
    ),

    FAITH(
        "Faith",
        Brush.linearGradient(colors = listOf(Color(0xFF01E264), Color(0xFF09FC15))),
        "Faith themed image",
        imageRes = Icons.faith
    ),

    FAMOUS(
        "Famous",
        Brush.linearGradient(colors = listOf(Color(0xFF01E265), Color(0xFF09FC16))),
        "Famous themed image",
        imageRes = Icons.famous
    ),

    FEAR(
        "Fear",
        Brush.linearGradient(colors = listOf(Color(0xFF01E266), Color(0xFF09FC17))),
        "Fear themed image",
        imageRes = Icons.fear
    ),

    FINANCE(
        "Finance",
        Brush.linearGradient(colors = listOf(Color(0xFF01E267), Color(0xFF09FC18))),
        "Finance themed image",
        imageRes = Icons.finance
    ),

    FITNESS(
        "Fitness",
        Brush.linearGradient(colors = listOf(Color(0xFF01E268), Color(0xFF09FC19))),
        "Fitness themed image",
        imageRes = Icons.fitness
    ),

    FOOD(
        "Food",
        Brush.linearGradient(colors = listOf(Color(0xFF01E269), Color(0xFF09FC1A))),
        "Food themed image",
        imageRes = Icons.food
    ),

    FORGIVENESS(
        "Forgiveness",
        Brush.linearGradient(colors = listOf(Color(0xFF01E26A), Color(0xFF09FC1B))),
        "Forgiveness themed image",
        imageRes = Icons.forgiveness
    ),

    FREEDOM(
        "Freedom",
        Brush.linearGradient(colors = listOf(Color(0xFF01E26B), Color(0xFF09FC1C))),
        "Freedom themed image",
        imageRes = Icons.freedom
    ),

    FUTURE(
        "Future",
        Brush.linearGradient(colors = listOf(Color(0xFF01E26C), Color(0xFF09FC1D))),
        "Future themed image",
        imageRes = Icons.future
    ),

    GOD(
        "God",
        Brush.linearGradient(colors = listOf(Color(0xFF01E26D), Color(0xFF09FC1E))),
        "God themed image",
        imageRes = Icons.god
    ),

    GOOD(
        "Good",
        Brush.linearGradient(colors = listOf(Color(0xFF01E26E), Color(0xFF09FC1F))),
        "Good themed image",
        imageRes = Icons.good
    ),

    GOVERNMENT(
        "Government",
        Brush.linearGradient(colors = listOf(Color(0xFF01E26F), Color(0xFF09FC20))),
        "Government themed image",
        imageRes = Icons.government
    ),

    HEALTH(
        "Health",
        Brush.linearGradient(colors = listOf(Color(0xFF01E270), Color(0xFF09FC21))),
        "Health themed image",
        imageRes = Icons.health
    ),

    HISTORY(
        "History",
        Brush.linearGradient(colors = listOf(Color(0xFF01E271), Color(0xFF09FC22))),
        "History themed image",
        imageRes = Icons.history
    ),

    HOME(
        "Home",
        Brush.linearGradient(colors = listOf(Color(0xFF01E272), Color(0xFF09FC23))),
        "Home themed image",
        imageRes = Icons.home
    ),

    DIET(
        "Diet",
        Brush.linearGradient(colors = listOf(Color(0xFF01E273), Color(0xFF09FC24))),
        "Diet themed image",
        imageRes = Icons.diet
    ),

    DREAMS(
        "Dreams",
        Brush.linearGradient(colors = listOf(Color(0xFF01E274), Color(0xFF09FC25))),
        "Dreams themed image",
        imageRes = Icons.dreams
    ),

    COOL(
        "Cool",
        Brush.linearGradient(colors = listOf(Color(0xFF01E275), Color(0xFF09FC26))),
        "Cool themed image",
        imageRes = Icons.cool
    ),

    FATHERS_DAY(
        "Father's Day",
        Brush.linearGradient(colors = listOf(Color(0xFF01E276), Color(0xFF09FC27))),
        "Father's Day themed image",
        imageRes = Icons.men
    ),

    GRADUATION(
        "Graduation",
        Brush.linearGradient(colors = listOf(Color(0xFF01E277), Color(0xFF09FC28))),
        "Graduation themed image",
        imageRes = Icons.graduation
    ),

    HOPE(
        "Hope",
        Brush.linearGradient(colors = listOf(Color(0xFF01E278), Color(0xFF09FC29))),
        "Hope themed image",
        imageRes = Icons.hope
    ),

    IMAGINATION(
        "Imagination",
        Brush.linearGradient(colors = listOf(Color(0xFF01E279), Color(0xFF09FC2A))),
        "Imagination themed image",
        imageRes = Icons.imagination
    ),

    INTELLIGENCE(
        "Intelligence",
        Brush.linearGradient(colors = listOf(Color(0xFF01E27A), Color(0xFF09FC2B))),
        "Intelligence themed image",
        imageRes = Icons.intelligence
    ),

    JEALOUSLY(
        "Jealously",
        Brush.linearGradient(colors = listOf(Color(0xFF01E27B), Color(0xFF09FC2C))),
        "Jealously themed image",
        imageRes = Icons.jealously
    ),

    KNOWLEDGE(
        "Knowledge",
        Brush.linearGradient(colors = listOf(Color(0xFF01E27C), Color(0xFF09FC2D))),
        "Knowledge themed image",
        imageRes = Icons.knowledge
    ),

    LEADERSHIP(
        "Leadership",
        Brush.linearGradient(colors = listOf(Color(0xFF01E27D), Color(0xFF09FC2E))),
        "Leadership themed image",
        imageRes = Icons.leadership
    ),

    LEARNING(
        "Learning",
        Brush.linearGradient(colors = listOf(Color(0xFF01E27E), Color(0xFF09FC2F))),
        "Learning themed image",
        imageRes = Icons.learning
    ),

    LEGAL(
        "Legal",
        Brush.linearGradient(colors = listOf(Color(0xFF01E27F), Color(0xFF09FC30))),
        "Legal themed image",
        imageRes = Icons.legal
    ),

    MARRIAGE(
        "Marriage",
        Brush.linearGradient(colors = listOf(Color(0xFF01E280), Color(0xFF09FC31))),
        "Marriage themed image",
        imageRes = Icons.marriage
    ),

    MEDICAL(
        "Medical",
        Brush.linearGradient(colors = listOf(Color(0xFF01E281), Color(0xFF09FC32))),
        "Medical themed image",
        imageRes = Icons.medical
    ),

    MEMORIAL_DAY(
        "Memorial Day",
        Brush.linearGradient(colors = listOf(Color(0xFF01E282), Color(0xFF09FC33))),
        "Memorial Day themed image",
        imageRes = Icons.memorial_day
    ),

    MEN(
        "Men",
        Brush.linearGradient(colors = listOf(Color(0xFF01E283), Color(0xFF09FC34))),
        "Men themed image",
        imageRes = Icons.men
    ),

    MOM(
        "Mom",
        Brush.linearGradient(colors = listOf(Color(0xFF01E284), Color(0xFF09FC35))),
        "Mom themed image",
        imageRes = Icons.mom
    ),

    MONEY(
        "Money",
        Brush.linearGradient(colors = listOf(Color(0xFF01E285), Color(0xFF09FC36))),
        "Money themed image",
        imageRes = Icons.money
    ),

    MORNING(
        "Morning",
        Brush.linearGradient(colors = listOf(Color(0xFF01E286), Color(0xFF09FC37))),
        "Morning themed image",
        imageRes = Icons.morning
    ),

    MOTHERS_DAY(
        "Mother's Day",
        Brush.linearGradient(colors = listOf(Color(0xFF01E287), Color(0xFF09FC38))),
        "Mother's Day themed image",
        imageRes = Icons.motherday
    ),

    MOVIES(
        "Movies",
        Brush.linearGradient(colors = listOf(Color(0xFF01E288), Color(0xFF09FC39))),
        "Movies themed image",
        imageRes = Icons.movie
    ),

    MUSIC(
        "Music",
        Brush.linearGradient(colors = listOf(Color(0xFF01E289), Color(0xFF09FC3A))),
        "Music themed image",
        imageRes = Icons.music
    ),

    NATURE(
        "Nature",
        Brush.linearGradient(colors = listOf(Color(0xFF01E28A), Color(0xFF09FC3B))),
        "Nature themed image",
        imageRes = Icons.nature
    ),

    NEW_YEARS(
        "New Years",
        Brush.linearGradient(colors = listOf(Color(0xFF01E28B), Color(0xFF09FC3C))),
        "New Years themed image",
        imageRes = Icons.new_years
    ),

    PARENTING(
        "Parenting",
        Brush.linearGradient(colors = listOf(Color(0xFF01E28C), Color(0xFF09FC3D))),
        "Parenting themed image",
        imageRes = Icons.parenting
    ),

    PATIENCE(
        "Patience",
        Brush.linearGradient(colors = listOf(Color(0xFF01E28D), Color(0xFF09FC3E))),
        "Patience themed image",
        imageRes = Icons.patience
    ),

    PATRIOTISM(
        "Patriotism",
        Brush.linearGradient(colors = listOf(Color(0xFF01E28E), Color(0xFF09FC3F))),
        "Patriotism themed image",
        imageRes = Icons.patriotism
    ),

    PEACE(
        "Peace",
        Brush.linearGradient(colors = listOf(Color(0xFF01E28F), Color(0xFF09FC40))),
        "Peace themed image",
        imageRes = Icons.peace
    ),

    PET(
        "Pet",
        Brush.linearGradient(colors = listOf(Color(0xFF01E290), Color(0xFF09FC41))),
        "Pet themed image",
        imageRes = Icons.pet
    ),

    POETRY(
        "Poetry",
        Brush.linearGradient(colors = listOf(Color(0xFF01E291), Color(0xFF09FC42))),
        "Poetry themed image",
        imageRes = Icons.poetry
    ),

    POWER(
        "Power",
        Brush.linearGradient(colors = listOf(Color(0xFF01E292), Color(0xFF09FC43))),
        "Power themed image",
        imageRes = Icons.power
    ),

    REALTIONSHIP(
        "Realtionship",
        Brush.linearGradient(colors = listOf(Color(0xFF01E293), Color(0xFF09FC44))),
        "Realtionship themed image",
        imageRes = Icons.religion
    ),

    RELIGION(
        "Religion",
        Brush.linearGradient(colors = listOf(Color(0xFF01E294), Color(0xFF09FC45))),
        "Religion themed image",
        imageRes = Icons.religion
    ),

    RESPECT(
        "Respect",
        Brush.linearGradient(colors = listOf(Color(0xFF01E295), Color(0xFF09FC46))),
        "Respect themed image",
        imageRes = Icons.respect
    ),

    ROMANTIC(
        "Romantic",
        Brush.linearGradient(colors = listOf(Color(0xFF01E296), Color(0xFF09FC47))),
        "Romantic themed image",
        imageRes = Icons.romantic
    ),

    SAD(
        "Sad",
        Brush.linearGradient(colors = listOf(Color(0xFF01E297), Color(0xFF09FC48))),
        "Sad themed image",
        imageRes = Icons.sad
    ),

    SCIENCE(
        "Science",
        Brush.linearGradient(colors = listOf(Color(0xFF01E298), Color(0xFF09FC49))),
        "Science themed image",
        imageRes = Icons.technology
    ),

    SOCIETY(
        "Society",
        Brush.linearGradient(colors = listOf(Color(0xFF01E299), Color(0xFF09FC4A))),
        "Society themed image",
        imageRes = Icons.socity
    ),

    SPORTS(
        "Sports",
        Brush.linearGradient(colors = listOf(Color(0xFF01E29A), Color(0xFF09FC4B))),
        "Sports themed image",
        imageRes = Icons.sports
    ),

    STRENGTH(
        "Strength",
        Brush.linearGradient(colors = listOf(Color(0xFF01E29B), Color(0xFF09FC4C))),
        "Strength themed image",
        imageRes = Icons.faith
    ),

    SYMPATHY(
        "Sympathy",
        Brush.linearGradient(colors = listOf(Color(0xFF01E29C), Color(0xFF09FC4D))),
        "Sympathy themed image",
        imageRes = Icons.sympathy
    ),

    TEACHER(
        "Teacher",
        Brush.linearGradient(colors = listOf(Color(0xFF01E29D), Color(0xFF09FC4E))),
        "Teacher themed image",
        imageRes = Icons.teacher
    ),

    TECHNOLOGY(
        "Technology",
        Brush.linearGradient(colors = listOf(Color(0xFF01E29E), Color(0xFF09FC4F))),
        "Technology themed image",
        imageRes = Icons.technology
    ),

    TEEN(
        "Teen",
        Brush.linearGradient(colors = listOf(Color(0xFF01E29F), Color(0xFF09FC50))),
        "Teen themed image",
        imageRes = Icons.power
    ),

    THANKFUL(
        "Thankful",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2A0), Color(0xFF09FC51))),
        "Thankful themed image",
        imageRes = Icons.famous
    ),

    THANKSGIVING(
        "Thanksgiving",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2A1), Color(0xFF09FC52))),
        "Thanksgiving themed image",
        imageRes = Icons.happiness
    ),

    TIME(
        "Time",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2A2), Color(0xFF09FC53))),
        "Time themed image",
        imageRes = Icons.time
    ),

    TRAVEL(
        "Travel",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2A3), Color(0xFF09FC54))),
        "Travel themed image",
        imageRes = Icons.future
    ),

    TRUST(
        "Trust",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2A4), Color(0xFF09FC55))),
        "Trust themed image",
        imageRes = Icons.age
    ),

    TRUTH(
        "Truth",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2A5), Color(0xFF09FC56))),
        "Truth themed image",
        imageRes = Icons.happiness
    ),

    WAR(
        "War",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2A6), Color(0xFF09FC57))),
        "War themed image",
        imageRes = Icons.war
    ),

    WEDDING(
        "Wedding",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2A7), Color(0xFF09FC58))),
        "Wedding themed image",
        imageRes = Icons.marriage
    ),

    WOMEN(
        "Women",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2A8), Color(0xFF09FC59))),
        "Women themed image",
        imageRes = Icons.women
    ),

    WORK(
        "Work",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2A9), Color(0xFF09FC5A))),
        "Work themed image",
        imageRes = Icons.socity
    ),

    DESIGN(
        "Design",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2AA), Color(0xFF09FC5B))),
        "Design themed image",
        imageRes = Icons.design
    ),

    DEATH(
        "Death",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2AB), Color(0xFF09FC5C))),
        "Death themed image",
        imageRes = Icons.death
    ),

    GREAT(
        "Great",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2AC), Color(0xFF09FC5D))),
        "Great themed image",
        imageRes = Icons.great
    ),

    MOVING_ON(
        "Moving On",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2AD), Color(0xFF09FC5E))),
        "Moving On themed image",
        imageRes = Icons.cool
    ),

    POLITICS(
        "Politics",
        Brush.linearGradient(colors = listOf(Color(0xFF01E2AE), Color(0xFF09FC5F))),
        "Politics themed image",
        imageRes = Icons.politics
    )
}