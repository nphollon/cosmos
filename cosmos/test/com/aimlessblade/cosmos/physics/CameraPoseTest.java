package com.aimlessblade.cosmos.physics;

import org.junit.Before;
import org.junit.Test;

import static com.aimlessblade.cosmos.physics.Identity.assertMatrixEquality;

public class CameraPoseTest {
    private static final Displacement DEFAULT_DISPLACEMENT = Vectors.position(0, 0, 0);
    private static final Orientation DEFAULT_ORIENTATION = Vectors.rotation(0, 0, 0);
    private Movable testPose;

    @Before
    public void setup() {
        testPose = new CameraPose(DEFAULT_DISPLACEMENT, DEFAULT_ORIENTATION);
    }

    @Test
    public void toMatrixShouldMultiplyOrientationByDisplacement() {
        Displacement displacement = Vectors.position(5, 4, 3);
        Orientation orientation = Vectors.rotation(8, 9, 10);
        Movable pose = new CameraPose(displacement, orientation);
        assertMatrixEquality(pose.toMatrix(), orientation.toMatrix().mult(displacement.toMatrix()));
    }

    @Test
    public void evolveShouldDoNothingIfNoImpulses() {
        final Pose otherPose = Vectors.pose(DEFAULT_DISPLACEMENT, DEFAULT_ORIENTATION);

        testPose.evolve(10);

        assertMatrixEquality(testPose, otherPose);
    }

    @Test
    public void impulseShouldSetNegativeVelocity() {
        testPose.impulse(Vectors.velocity(1, 2, 3));
        testPose.evolve(1);

        final Pose expectedFinalPose = Vectors.pose(Vectors.position(-1, -2, -3), DEFAULT_ORIENTATION);

        assertMatrixEquality(testPose, expectedFinalPose);
    }

    @Test
    public void velocityShouldNotBeAppliedUntilEvolveIsCalled() {
        testPose.impulse(Vectors.velocity(1, 2, 3));

        final Pose expectedFinalPose = Vectors.pose(DEFAULT_DISPLACEMENT, DEFAULT_ORIENTATION);

        assertMatrixEquality(testPose, expectedFinalPose);
    }

    @Test
    public void velocityShouldBeAppliedEachTimeEvolveIsCalled() {
        testPose.impulse(Vectors.velocity(1, 4, 9));
        testPose.evolve(1);
        testPose.evolve(1);

        final Pose expectedFinalPose = Vectors.pose(-2, -8, -18, 0, 0, 0);

        assertMatrixEquality(testPose, expectedFinalPose);
    }
    
    @Test
    public void displacementShouldChangeByVelocityTimesTime() {
        testPose.impulse(Vectors.velocity(2, 3, 7));
        testPose.evolve(0.5);

        final Pose expectedFinalPose = Vectors.pose(-1, -1.5, -3.5, 0, 0, 0);

        assertMatrixEquality(testPose, expectedFinalPose);
    }

    @Test
    public void evolveShouldChangePoseByTheSumOfVelocities() {
        testPose.impulse(Vectors.velocity(-1, 8, -5.2));
        testPose.impulse(Vectors.velocity(1, -8, 5.2));
        testPose.evolve(10);

        final Pose expectedFinalPose = Vectors.pose(DEFAULT_DISPLACEMENT, DEFAULT_ORIENTATION);

        assertMatrixEquality(testPose, expectedFinalPose);
    }

    @Test
    public void angularImpulseShouldSetAngularVelocity() {
        final Velocity angularVelocity = Vectors.velocity(3, -7, 1);
        testPose.angularImpulse(angularVelocity);
        testPose.evolve(2);

        final Orientation expectedOrientation = new QuaternionOrientation(angularVelocity.negative().overTime(2));
        final Pose expectedFinalPose = Vectors.pose(DEFAULT_DISPLACEMENT, expectedOrientation);

        assertMatrixEquality(testPose, expectedFinalPose);
    }

    @Test
    public void angularImpulsesShouldStack() {
        final Velocity angularVelocity = Vectors.velocity(3, -7, 1);
        testPose.angularImpulse(angularVelocity);
        testPose.angularImpulse(angularVelocity);
        testPose.evolve(1);

        final Orientation expectedOrientation = new QuaternionOrientation(angularVelocity.negative().overTime(2));
        final Pose expectedFinalPose = Vectors.pose(DEFAULT_DISPLACEMENT, expectedOrientation);

        assertMatrixEquality(testPose, expectedFinalPose);
    }
}
