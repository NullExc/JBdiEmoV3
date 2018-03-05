package sk.tuke.fei.bdi.emotionalengine.res;



import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Zemianek
 * @author Tomáš Herich
 */
public class R {

    // -------------------------------------------------------------------------------
    // Additional JBDIEmo parameters used in v2
    // -------------------------------------------------------------------------------

    // BDIV2 Services required for emotional agent
    public final static String MESSAGE_SERVICE = "v2_message_service";
    public final static String COMPONENT_SERVICE = "v2_CMS";

    // BDIV2 keys for Map represented as emotional message
    public final static String KEY_MESSAGE_EMOTIONAL_SEND = "key_emotional_message_send";
    public final static String KEY_MESSAGE_EMOTIONAL_RECEIVE = "key_emotional_message_receive";
    public final static String KEY_MESSAGE_EMOTIONAL = "key_emotional_message";
    public final static String KEY_MESSAGE_PLAN = "key_message_plan";
    public final static String KEY_MESSAGE_RESULT = "key_result";
    public final static String KEY_SENDER_ID = "key_sender_id";
    public final static String KEY_PLAN_NAME = "key_plan_name";

    // BDIV2 emotional parameter attribute keys
    public final static String FIELD = "v2_field_target";
    public final static String METHOD = "v2_method_target";
    public final static String BOOLEAN = "v2_simple_boolean";
    public final static String STRING = "v2_simple_string";
    public final static String DOUBLE = "v2_simple_double";

    //BDIV2

    // -------------------------------------------------------------------------------
    // Emotional engine, Emotional objectValue types, Emotional event types
    // -------------------------------------------------------------------------------

    // Name of belief storing Emotion engine
    public final static String ENGINE = "emotional_engine";

    // Emotional engine initialization parameters
    public final static String ENGINE_PARAM_GUI = "gui_enabled";
    public final static String ENGINE_PARAM_LOGGER = "logger_enabled";
    public final static String ENGINE_PARAM_LOGGER_LOGGING_DELAY_MILLIS = "logging_delay_millis";
    public final static String ENGINE_PARAM_DECAY_TIME_MILLIS = "decay_time_millis";
    public final static String ENGINE_PARAM_DECAY_STEPS_TO_MIN = "decay_steps_to_min";
    public final static String ENGINE_PARAM_EMOTIONAL_OTHERS = "emotional_others";

    // Emotional objectValue types
    public final static int PLAN = 1;
    public final static int GOAL = 2;
    public final static int BELIEF = 3;
    public final static int BELIEF_SET = 4;
    public final static int BELIEF_SET_BELIEF = 5;

    // Emotional objectValue type names
    public static final Map<Integer, String> EMOTIONAL_ELEMENT_TYPE_NAMES;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(PLAN, "plan");
        map.put(GOAL, "goal");
        map.put(BELIEF, "belief");
        map.put(BELIEF_SET, "belief set");
        map.put(BELIEF_SET_BELIEF, "belief set belief");
        EMOTIONAL_ELEMENT_TYPE_NAMES = Collections.unmodifiableMap(map);
    }

    // Emotional objectValue type long names
    public static final Map<Integer, String> EMOTIONAL_ELEMENT_TYPE_LONG_NAMES;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(PLAN, "plan (actions of agents)");
        map.put(GOAL, "goal (consequences of events)");
        map.put(BELIEF, "belief (aspects of objects)");
        map.put(BELIEF_SET, "belief set (set of beliefs)");
        map.put(BELIEF_SET_BELIEF, "belief (belonging to belief set)");
        EMOTIONAL_ELEMENT_TYPE_LONG_NAMES = Collections.unmodifiableMap(map);
    }

    // Emotional event types
    public final static int EVT_GOAL_CREATED = 1;
    public final static int EVT_GOAL_FINISHED = 2;
    public final static int EVT_PLAN_CREATED = 3;
    public final static int EVT_PLAN_FINISHED = 4;
    public final static int EVT_BELIEF_CHANGED = 5;

    // Emotional event type names
    public static final Map<Integer, String> EMOTIONAL_EVENT_TYPE_NAMES;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(EVT_GOAL_CREATED, "goal created");
        map.put(EVT_GOAL_FINISHED, "goal finished");
        map.put(EVT_PLAN_CREATED, "plan created");
        map.put(EVT_PLAN_FINISHED, "plan finished");
        map.put(EVT_BELIEF_CHANGED, "belief changed");
        EMOTIONAL_EVENT_TYPE_NAMES = Collections.unmodifiableMap(map);
    }

    // Emotional result types
    public final static int RESULT_NULL = 1;
    public final static int RESULT_SUCCESS = 2;
    public final static int RESULT_FAILURE = 3;

    // Emotional result type names
    public static final Map<Integer, String> EMOTIONAL_RESULT_TYPE_NAMES;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(RESULT_NULL, "no result (adopted event)");
        map.put(RESULT_SUCCESS, "success");
        map.put(RESULT_FAILURE, "failure");
        EMOTIONAL_RESULT_TYPE_NAMES = Collections.unmodifiableMap(map);
    }


    // -------------------------------------------------------------------------------
    // Emotional parameters
    // -------------------------------------------------------------------------------

    // Emotional parameter name for agent ADF mapping
    public static final String PARAM_EMOTIONAL = "emotional";

    // Emotional parameter types
    public final static String PARAM_DESIRABILITY = "desirability";
    public final static String PARAM_PROBABILITY = "probability";
    public final static String PARAM_APPROVAL = "approval";
    public final static String PARAM_DISAPPROVAL = "disapproval";
    public final static String PARAM_OTHER_DESIRE_GOAL_SUCCESS = "other_desire_success";
    public final static String PARAM_OTHER_DESIRE_GOAL_FAILURE = "other_desire_failure";
    public final static String PARAM_EMOTIONAL_OTHER = "emotional_other";
    public final static String PARAM_EMOTIONAL_OTHER_GROUP = "emotional_other_group";
    public final static String PARAM_EMOTIONAL_OTHER_PLAN = "emotional_other_plan";


    // Emotional parameter types holder
    public final static String[] EMOTIONAL_PARAMETERS = new String[] {
            PARAM_PROBABILITY,
            PARAM_DESIRABILITY,
            PARAM_APPROVAL,
            PARAM_DISAPPROVAL,
            PARAM_OTHER_DESIRE_GOAL_SUCCESS,
            PARAM_OTHER_DESIRE_GOAL_FAILURE,
            PARAM_EMOTIONAL_OTHER,
            PARAM_EMOTIONAL_OTHER_GROUP,
            PARAM_EMOTIONAL_OTHER_PLAN
    };

    // Emotional parameter long names
    public static final Map<String, String> EMOTIONAL_PARAMETER_LONG_NAMES;
    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put(PARAM_PROBABILITY, "probability (of success)");
        map.put(PARAM_DESIRABILITY, "desirability (of success)");
        map.put(PARAM_APPROVAL, "approval (of action)");
        map.put(PARAM_DISAPPROVAL, "disapproval (of action)");
        map.put(PARAM_OTHER_DESIRE_GOAL_SUCCESS, "other desire success (of my goal)");
        map.put(PARAM_OTHER_DESIRE_GOAL_FAILURE, "other desire failure (of my goal)");
        map.put(PARAM_EMOTIONAL_OTHER, "other agent (emotions are felt to)");
        map.put(PARAM_EMOTIONAL_OTHER_GROUP, "all other agents (same type)");
        map.put(PARAM_EMOTIONAL_OTHER_PLAN, "other agent's plan (emotions are felt to)");
        EMOTIONAL_PARAMETER_LONG_NAMES = Collections.unmodifiableMap(map);
    }

    // Emotional belief parameters
    public static final int BELIEF_FAMILIAR = 1;
    public static final int BELIEF_ATTRACTIVE = 2;
    public static final int BELIEF_INTENSITY = 3;

    // Emotional belief parameter long names
    public static final Map<Integer, String> EMOTIONAL_BELIEF_PARAMETER_LONG_NAMES;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(BELIEF_FAMILIAR, "is object familiar");
        map.put(BELIEF_ATTRACTIVE, "is object attractive");
        map.put(BELIEF_INTENSITY, "intensity of attractiveness");
        EMOTIONAL_BELIEF_PARAMETER_LONG_NAMES = Collections.unmodifiableMap(map);
    }

    // Emotional message parameters
    public static final String MESSAGE_EMOTIONAL_SEND = "emotional_message_send";
    public static final String MESSAGE_EMOTIONAL_RECEIVE = "emotional_message_receive";
    public static final String MESSAGE_EMOTIONAL = "emotional_message";
    public static final String MESSAGE_PLAN_CREATED = "plan_created";
    public static final String MESSAGE_PLAN_FINISHED = "plan_finished";
    public static final String MESSAGE_RESULT_NULL = "null";
    public static final String MESSAGE_RESULT_SUCCESS = "success";
    public static final String MESSAGE_RESULT_FAILURE = "failure";
    public static final String MESSAGE_DELIMITER = "|";
    public static final String MESSAGE_DELIMITER_REGEXP = "\\|";


    // -------------------------------------------------------------------------------
    // Calculator parameters
    // -------------------------------------------------------------------------------

    // PolynomialCalculator types
    public static final int CALC_LINEAR = 1;
    public static final int CALC_QUADRATIC = 2;
    public static final int CALC_CUBIC = 3;


    // -------------------------------------------------------------------------------
    // Emotion ID list
    // -------------------------------------------------------------------------------

    // Basic Emotions (moods)
    public static final int POSITIVE = 1;
    public static final int NEGATIVE = 2;

    // Goal Emotions
    public static final int PLEASED = 3;
    public static final int DISPLEASED = 4;
    public static final int HOPE = 5;
    public static final int FEAR = 6;
    public static final int JOY = 7;
    public static final int DISTRESS = 8;
    public static final int SATISFACTION = 9;
    public static final int DISAPPOINTMENT = 10;
    public static final int RELIEF = 11;
    public static final int FEAR_CONFIRMED = 12;
    public static final int HAPPY_FOR = 13;
    public static final int RESENTMENT = 14;
    public static final int GLOATING = 15;
    public static final int PITY = 16;

    // Plan (action of agent) Emotions
    public static final int APPROVING = 17;
    public static final int DISAPPROVING = 18;
    // Self
    public static final int PRIDE = 19;
    public static final int SHAME = 20;
    public static final int GRATIFICATION = 21;
    public static final int REMORSE = 22;
    // Other agents
    public static final int ADMIRATION = 23;
    public static final int REPROACH = 24;
    public static final int GRATITUDE = 25;
    public static final int ANGER = 26;

    // Belief (aspect of object) Emotions
    public static final int LIKING = 27;
    public static final int DISLIKING = 28;
    public static final int LOVE = 29;
    public static final int HATE = 30;
    public static final int INTEREST = 31;
    public static final int DISGUST = 32;

    // Emotions names
    public static final Map<Integer, String> EMOTIONAL_IDS_NAMES;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(POSITIVE, "positive");
        map.put(NEGATIVE, "negative");
        map.put(PLEASED, "pleased");
        map.put(DISPLEASED, "displeased");
        map.put(HOPE, "hope");
        map.put(FEAR, "fear");
        map.put(JOY, "joy");
        map.put(DISTRESS, "distress");
        map.put(SATISFACTION, "satisfaction");
        map.put(DISAPPOINTMENT, "disappointment");
        map.put(RELIEF, "relief");
        map.put(FEAR_CONFIRMED, "fear confirmed");
        map.put(HAPPY_FOR, "happy for");
        map.put(RESENTMENT, "resentment");
        map.put(GLOATING, "gloating");
        map.put(PITY, "pity");
        map.put(APPROVING, "approving");
        map.put(DISAPPROVING, "disapproving");
        map.put(PRIDE, "pride");
        map.put(SHAME, "shame");
        map.put(GRATIFICATION, "gratification");
        map.put(REMORSE, "remorse");
        map.put(ADMIRATION, "admiration");
        map.put(REPROACH, "reproach");
        map.put(GRATITUDE, "gratitude");
        map.put(ANGER, "anger");
        map.put(LIKING, "liking");
        map.put(DISLIKING, "disliking");
        map.put(LOVE, "love");
        map.put(HATE, "hate");
        map.put(INTEREST, "interest");
        map.put(DISGUST, "disgust");
        EMOTIONAL_IDS_NAMES = Collections.unmodifiableMap(map);
    }


    // -------------------------------------------------------------------------------
    // GUI constants
    // -------------------------------------------------------------------------------

    // Basic
    public static final int GUI_SPACING = 5;

    // Colors

    public static final Color GUI_LIGHT_BLUE = new Color(66, 183, 255);
    public static final Color GUI_BLUE = new Color(27, 66, 232);
    public static final Color GUI_PURPLE = new Color(122, 32, 244);

    public static final Color GUI_ORANGE = new Color(250, 140, 83);
    public static final Color GUI_RED = new Color(215, 21, 24);
    public static final Color GUI_MAGENTA = new Color(235, 39, 133);

    public static final Color GUI_PINK = new Color(255, 117, 193);
    public static final Color GUI_GREEN = new Color(45, 183, 56);

    public static final Color GUI_YELLOW = new Color(228, 179, 30);
    public static final Color GUI_TEAL = new Color(60, 186, 188);

    public static final Color GUI_SAND = new Color(212, 177, 120);
    public static final Color GUI_DARK_SAND = new Color(154, 117, 71);

    public static final Color GUI_GREY = new Color(167, 167, 167);
    public static final Color GUI_DARK_GREY = new Color(78, 78, 78);


}