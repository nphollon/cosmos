package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;

import static com.aimlessblade.cosmos.util.Assert.assertMatrixEquality;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PoseTest {
    private static final Displacement DEFAULT_DISPLACEMENT = Displacement.cartesian(0, 0, 0);
    private static final double TOLERANCE = 1e-5;

    private Pose testPose;

    @Before
    public void setup() {
        testPose = Pose.build(DEFAULT_DISPLACEMENT, Orientation.zero());
    }

    @Test
    public void matrixShouldBeDisplacementMatrixIfNoRotation() {
        final Displacement displacement = Displacement.cartesian(1, 2, 3);

        final Pose pose = Pose.build(displacement, Orientation.zero());

        assertMatrixEquality(pose.toMatrix(), displacement.toMatrix(), TOLERANCE);
    }

    @Test
    public void matrixShouldBeDisplacementTimesRotation() {
        final Displacement displacement = Displacement.cartesian(1, 2, 3);
        final Orientation orientation = Orientation.cartesian(90, 0, 0);

        final Pose pose = Pose.build(displacement, orientation);

        final SimpleMatrix expectedMatrix = displacement.toMatrix().mult(orientation.toMatrix());
        assertMatrixEquality(pose.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void posesShouldBeIdenticalIfOrientationsAndPositionsAre() {
        final Pose otherPose = Pose.build(Displacement.cartesian(1e-6, 0, 0), Orientation.cartesian(1e-6, 0, 0));

        assertPoseEquality(testPose, otherPose, TOLERANCE);
    }

    @Test
    public void posesShouldNotBeIdenticalIfOrientationsAreNot() {
        final Pose otherPose = Pose.build(DEFAULT_DISPLACEMENT, Orientation.cartesian(4, 5, 6));

        assertPoseEquality(testPose, otherPose, TOLERANCE, false);
    }

    @Test
    public void posesShouldNotBeIdenticalIfDisplacementsAreNot() {
        final Pose otherPose = Pose.build(Displacement.cartesian(5, 4, 3), Orientation.zero());

        assertPoseEquality(testPose, otherPose, TOLERANCE, false);
    }

    @Test
    public void evolveShouldDoNothingIfNoImpulses() {
        final Pose otherPose = Pose.build(DEFAULT_DISPLACEMENT, Orientation.zero());

        testPose.evolve(10);

        assertPoseEquality(testPose, otherPose, TOLERANCE);
    }

    @Test
    public void impulseShouldSetVelocity() {
        testPose.impulse(Velocity.cartesian(1, 2, 3));
        testPose.evolve(1);

        final Pose expectedFinalPose = Pose.build(Displacement.cartesian(1, 2, 3), Orientation.zero());

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void velocityShouldNotBeAppliedUntilEvolveIsCalled() {
        testPose.impulse(Velocity.cartesian(1, 2, 3));

        final Pose expectedFinalPose = Pose.build(DEFAULT_DISPLACEMENT, Orientation.zero());

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void velocityShouldBeAppliedEachTimeEvolveIsCalled() {
        testPose.impulse(Velocity.cartesian(1, 4, 9));
        testPose.evolve(1);
        testPose.evolve(1);

        final Pose expectedFinalPose = Pose.build(Displacement.cartesian(2, 8, 18), Orientation.zero());

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void displacementShouldChangeByVelocityTimesTime() {
        testPose.impulse(Velocity.cartesian(2, 3, 7));
        testPose.evolve(0.5);

        final Pose expectedFinalPose = Pose.build(Displacement.cartesian(1, 1.5, 3.5), Orientation.zero());

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void evolveShouldChangePoseByTheSumOfDisplacements() {
        testPose.impulse(Velocity.cartesian(-1, 8, -5.2));
        testPose.impulse(Velocity.cartesian(1, -8, 5.2));
        testPose.evolve(10);

        final Pose expectedFinalPose = Pose.build(DEFAULT_DISPLACEMENT, Orientation.zero());

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void angularImpulseShouldSetAngularVelocity() {
        final Velocity angularVelocity = Velocity.cartesian(3, -7, 1);
        testPose.angularImpulse(angularVelocity);
        testPose.evolve(2);

        final Orientation expectedOrientation = Orientation.rotationVector(angularVelocity.overTime(2));
        final Pose expectedFinalPose = Pose.build(DEFAULT_DISPLACEMENT, expectedOrientation);

        assertPoseEquality(testPose, expectedFinalPose, TOLERANCE);
    }

    @Test
    public void angularImpulsesShouldStack() {
        final Velocity angularVelocity = Velocity.cartesian(3, -7, 1);
        testPose.angularImpulse(angularVelocity);
        testPose.angularImpulse(angularVelocity);
        testPose.evolve(1);

        final Orientation expectedOrientation = Orientation.rotationVector(angularVelocity.overTime(2));
        final Pose expectedFinalPose = Pose.build(DEFAULT_DISPLACEMENT, expectedOrientation);

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
