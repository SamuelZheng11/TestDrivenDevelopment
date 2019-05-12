package Automation;

import GitHub.IBranch;

import java.io.File;

public interface AbstractionExtension {
    File generateCodeAbstractionFor(IBranch branch);
}
