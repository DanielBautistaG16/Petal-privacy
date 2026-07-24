import shutil

ORIG = "/home/dani/Petal/PetalApp-Android"
WORKTREE = "/home/dani/Petal/.claude/worktrees/quizzical-bardeen-299f95/PetalApp-Android"
BASE = "app/src/main/java/com/petal/handsfree"

files = [
    f"{BASE}/service/VoiceService.kt",
    f"{BASE}/processor/VoiceProcessor.kt",
    f"{BASE}/client/GeminiClient.kt",
]

for f in files:
    shutil.copy(f"{WORKTREE}/{f}", f"{ORIG}/{f}")
    print(f"Copied: {f}")

# Fix NavigationHandler missing val
nav = f"{ORIG}/{BASE}/utils/NavigationHandler.kt"
with open(nav) as fh:
    c = fh.read()
c = c.replace("private const TAG", "private const val TAG")
with open(nav, "w") as fh:
    fh.write(c)
print("NavigationHandler: const val fixed")

print("All done!")
