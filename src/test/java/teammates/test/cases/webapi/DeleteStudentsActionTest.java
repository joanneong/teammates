package teammates.test.cases.webapi;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.util.Const;
import teammates.ui.webapi.action.DeleteStudentsAction;
import teammates.ui.webapi.action.JsonResult;

/**
 * SUT: {@link DeleteStudentsAction}.
 */
public class DeleteStudentsActionTest extends BaseActionTest<DeleteStudentsAction> {

    @Override
    protected String getActionUri() {
        return Const.ResourceURIs.STUDENTS;
    }

    @Override
    protected String getRequestMethod() {
        return DELETE;
    }

    @Override
    @Test
    protected void testExecute() {
        InstructorAttributes instructor1OfCourse1 = typicalBundle.instructors.get("instructor1OfCourse1");

        ______TS("success: delete all students");
        loginAsInstructor(instructor1OfCourse1.googleId);

        String[] submissionParams = new String[] {
                Const.ParamsNames.COURSE_ID, instructor1OfCourse1.courseId,
        };

        DeleteStudentsAction action = getAction(submissionParams);
        JsonResult result = getJsonResult(action);

        assertEquals(HttpStatus.SC_OK, result.getStatusCode());

        ______TS("fails silently if random course given");
        submissionParams = new String[] {
                Const.ParamsNames.COURSE_ID, "RANDOM_ID",
        };

        action = getAction(submissionParams);
        result = getJsonResult(action);

        assertEquals(HttpStatus.SC_OK, result.getStatusCode());

        ______TS("failure: invalid params");

        verifyHttpParameterFailure();
    }

    @Override
    @Test
    protected void testAccessControl() {
        InstructorAttributes instructor1OfCourse1 = typicalBundle.instructors.get("instructor1OfCourse1");

        String[] submissionParams = new String[] {
                Const.ParamsNames.COURSE_ID, instructor1OfCourse1.courseId,
        };

        verifyInaccessibleWithoutModifyStudentPrivilege(submissionParams);
        verifyOnlyInstructorsOfTheSameCourseCanAccess(submissionParams);
    }

}
