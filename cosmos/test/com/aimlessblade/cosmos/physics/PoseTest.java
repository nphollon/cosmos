package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;

import static com.aimlessblade.cosmos.util.Assert.assertMatrixEquality;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PoseTest {
    private static final Displacement DEFAULT_DISPLACEMENT = Vectors.position(0, 0, 0);
    private static final Orientation DEFAULT_ORIENTATION = new RotationVectorOrientation(DEFAULT_DISPLACEMENT);
    private static final double TOLERANCE = 1e-5;

    private Pose testPose;

    @Before
    public void setup() {
        testPose = Vectors.pose(DEFAULT_DISPLACEMENT, DEFAULT_ORIENTATION);
    }

    @Test
    public void matrixShouldBeDisplacementMatrixIfNoRotation() {
        final Displacement displacement = Vectors.position(1, 2, 3);

        final Pose pose = Vectors.pose(displacement, DEFAULT_ORIENTATION);

        assertMatrixEquality(pose.toMatrix(), displacement.toMatrix(), TOLERANCE);
    }

    @Test
    public void matrixShouldBeDisplacementTimesRotation() {
        final Displacement displacement = Vectors.position(1, 2, 3);
        final Orientation orientation = Vectors.rotation(90, 0, 0);

        final Pose pose = Vectors.pose(1, 2, 3, 90, 0, 0);

        final SimpleMatrix expectedMatrix = displacement.toMatrix().mult(orientation.toMatrix());
        assertMatrixEquality(pose.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void posesShouldBeIdenticalIfOrientationsAndPositionsAre() {
        final Pose otherPose = Vectors.pose(1e-6, 0, 0, 1e-6, 0, 0);

        assertPoseEquality(testPose, otherPose, TOLERANCE);
    }

    @Test
    public void posesShouldNotBeIdenticalIfOrientationsAreNot() {
        final Pose otherPose = Vectors.pose(0, 0, 0, 4, 5, 6);

        assertPoseEquality(testPose, otherPose, TOLERANCE, false);
    }

    @Test
    public void posesShouldNotBeIdenticalIfDisplacementsAreNot() {
        final Pose otherPose = Vectors.pose(5, 4, 3, 0, 0, 0);

        assertPoseEquality(testPose, otherPose, TOLERANCE, false);
    }

    @Test
    public void evolveShouldDoNothingIfNoImpulses() {
        final Pose otherPose = Vectors.pose(DEFAULT_DISPLACEMENT, DEFAULT_ORIENTATION);

        testPose.evolve(10);

        assertPoseEquality(testPose, otherPose, TOLERANCE);
    }

    @Test
    public void impulseShouldSetVelocity() {
        testPose.impulse(Vectors.velocity(1, 2, 3));
        testPose.evolve(1);

        final Pose expectedFinalPose = Vectors.pose(Vectors.position(1, 2, 3), DEFAULT_ORIENTATION);

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void velocityShouldNotBeAppliedUntilEvolveIsCalled() {
        testPose.impulse(Vectors.velocity(1, 2, 3));

        final Pose expectedFinalPose = Vectors.pose(DEFAULT_DISPLACEMENT, DEFAULT_ORIENTATION);

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void velocityShouldBeAppliedEachTimeEvolveIsCalled() {
        testPose.impulse(Vectors.velocity(1, 4, 9));
        testPose.evolve(1);
        testPose.evolve(1);

        final Pose expectedFinalPose = Vectors.pose(2, 8, 18, 0, 0, 0);

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void displacementShouldChangeByVelocityTimesTime() {
        testPose.impulse(Vectors.velocity(2, 3, 7));
        testPose.evolve(0.5);

        final Pose expectedFinalPose = Vectors.pose(1, 1.5, 3.5, 0, 0, 0);

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void evolveShouldChangePoseByTheSumOfDisplacements() {
        testPose.impulse(Vectors.velocity(-1, 8, -5.2));
        testPose.impulse(Vectors.velocity(1, -8, 5.2));
        testPose.evolve(10);

        final Pose expectedFinalPose = Vectors.pose(DEFAULT_DISPLACEMENT, DEFAULT_ORIENTATION);

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void angularImpulseShouldSetAngularVelocity() {
        final Velocity angularVelocity = Vectors.velocity(3, -7, 1);
        testPose.angularImpulse(angularVelocity);
        testPose.evolve(2);

        final Orientation expectedOrientation = new RotationVectorOrientation(angularVelocity.overTime(2));
        final Pose expectedFinalPose = Vectors.pose(DEFAULT_DISPLACEMENT, expectedOrientation);

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void angularImpulsesShouldStack() {
        final Velocity angularVelocity = Vectors.velocity(3, -7, 1);
        testPose.angularImpulse(angularVelocity);
        testPose.angularImpulse(angularVelocity);
        testPose.evolve(1);

        final Orientation expectedOrientation = new RotationVectorOrientation(angularVelocity.overTime(2));
        final Pose expectedFinalPose = Vectors.pose(DEFAULT_DISPLACEMENT, expectedOrientation);

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    public static void assertPoseEquality(final Pose pose1, final Pose pose2, final double tolerance) {
        assertPoseEquality(pose1, pose2, tolerance, true);
    }

    public static void assertPoseEquality(final Pose pose1, final Pose pose2, final double tolerance, final boolean expected) {
        String message = "\nActual:\n" + pose1 + "\nExpected:\n" + pose2;
        assertThat(message, pose1.isIdentical(pose2, tolerance), is(expected));
        assertThat(message, pose2.isIdentical(pose1, tolerance), is(expected));
    }
}
