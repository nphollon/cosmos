package com.aimlessblade.cosmos.physics;

public final class Vectors {

    public static Orientation rotation(final double rx, final double ry, final double rz) {
        return new RotationVectorOrientation(position(rx, ry, rz));
    }

    public static Displacement position(final double x, final double y, final double z) {
        return new CartesianDisplacement(x, y, z);
    }

    public static Velocity velocity(final double vx, final double vy, final double vz) {
        return new CartesianVelocity(vx, vy, vz);
    }

    public static Pose pose(Displacement displacement, Orientation orientation) {
        return new Pose(displacement, orientation);
    }

    public static Pose pose(final double x, final double y, final double z,
                            final double rx, final double ry, final double rz) {
        final Displacement displacement = position(x, y, z);
        final Orientation orientation = rotation(rx, ry, rz);
        return new Pose(displacement, orientation);
    }

    private Vectors() {}
}
