# How to Run the Tests

The same tests can run in two places. Only the command changes; the test code is identical.

| Mode | Where the browser runs | Needs |
|---|---|---|
| Local | Firefox on your own machine | Firefox installed |
| Selenoid | Firefox inside a Docker container | Docker Desktop + Selenoid running |

There is one driver manager per mode, in `src/main/java/master/driver/`:

| Class | Creates |
|---|---|
| `LocalDriverManager` | `FirefoxDriver` on this machine |
| `SelenoidDriverManager` | `RemoteWebDriver` pointing at Selenoid / Grid |
| `DriverManagerTL` | Picks one of the two and keeps the driver for the current thread |

There is also one TestNG suite file per mode, in the project root:

| Suite file | Runs on | How it selects the mode |
|---|---|---|
| `testng-local.xml` | Local Firefox | Has no `grid.url` parameter |
| `testng-selenoid.xml` | Selenoid | Has `<parameter name="grid.url" value="http://localhost:4444/wd/hub"/>` |
| `testng.xml` | Local Firefox (original file, still used by the Jenkins job) | Has no `grid.url` parameter |

`DriverManagerTL.init()` picks by the Grid URL:

- no Grid URL → `LocalDriverManager`
- Grid URL set → `SelenoidDriverManager`

The Grid URL comes from the suite file. `-Dgrid.url=...` on the command line overrides it, so any
suite can be sent to Selenoid without editing the file.

If Selenoid runs on another machine or port, change the `value` in `testng-selenoid.xml`.

> On PowerShell, keep the quotes around each `-D...` argument. On `cmd` or Git Bash the quotes are optional.
> The `X` in `suiteXmlFiles` must be a capital letter, otherwise Maven ignores `testng.xml`.

---

## 1. Run on Local

### Prerequisites

- JDK 16 or newer (`java -version`)
- Maven (`mvn -version`)
- Firefox installed

No driver download is needed. Firefox still needs `geckodriver` to be driven, but Selenium Manager
(built into Selenium 4.6+) downloads it automatically on the first run and caches it under
`%USERPROFILE%\.cache\selenium`.

### Run the whole suite

```
mvn test "-Dsurefire.suiteXmlFiles=testng-local.xml"
```

A Firefox window opens for each test.

### Run without a browser window (headless)

```
mvn test "-Dsurefire.suiteXmlFiles=testng-local.xml" "-Dheadless=true"
```

### Run a single test

```
mvn test "-Dtest=LoginTest#testLoginPositive"
mvn test "-Dtest=LoginTest#testLoginPNegative"
```

### Run from IntelliJ

Right-click `testng-local.xml` → **Run**. It runs on local Firefox.
Right-click `testng-selenoid.xml` → **Run** to run on Selenoid instead (Selenoid must be running).
To pass options, edit the run configuration and add to **VM options**, for example `-Dheadless=true`.

---

## 2. Run on Selenoid

### One-time setup

1. Install and start **Docker Desktop**. Check it is running:

   ```
   docker ps
   ```

2. Download the Selenoid Configuration Manager (`cm`) from
   https://github.com/aerokube/cm/releases and put it on your `PATH` as `cm`.

3. Start Selenoid. This downloads the browser images (several GB) the first time:

   ```
   cm selenoid start --vnc
   ```

   Use two dashes: `--vnc`. With one dash (`-vnc`) the tool reads it as "version nc" and fails with
   `aerokube/selenoid:nc: not found`.

   If Selenoid starts but no browser images were downloaded, force it:

   ```
   cm selenoid start --vnc --force
   ```

4. Start the Selenoid UI (optional, lets you watch the browser):

   ```
   cm selenoid-ui start
   ```

### Check Selenoid is ready

```
docker ps
```

You should see `selenoid` on port `4444` and `selenoid-ui` on port `8080`.

Open http://localhost:4444/status — it lists the available browsers.

### Run the whole suite on Selenoid

```
mvn test "-Dsurefire.suiteXmlFiles=testng-selenoid.xml"
```

No `-Dgrid.url` is needed; the suite file carries the Selenoid URL.

### Run a single test on Selenoid

A single test does not go through a suite file, so pass the URL on the command line:

```
mvn test "-Dtest=LoginTest#testLoginPositive" "-Dgrid.url=http://localhost:4444/wd/hub"
```

### Watch the test live

1. Open http://localhost:8080
2. Run the Maven command above
3. A session appears in the UI. Click it to see the live browser screen.

Sessions disappear when the test finishes (about 30 seconds), so open the UI first.

Do not add `-Dheadless=true` on Selenoid. A headless browser draws nothing, so the live view stays empty.

### Stop Selenoid

```
cm selenoid stop
cm selenoid-ui stop
```

---

## Options at a glance

| Option | Effect |
|---|---|
| `-Dsurefire.suiteXmlFiles=testng-local.xml` | Run the suite on local Firefox |
| `-Dsurefire.suiteXmlFiles=testng-selenoid.xml` | Run the suite on Selenoid |
| `-Dtest=LoginTest#methodName` | Run one test method |
| `-Dheadless=true` | Local Firefox with no window |
| `-Dgrid.url=http://localhost:4444/wd/hub` | Run on Selenoid whatever the suite file says (overrides the suite value) |
| `-Dwebdriver.gecko.driver=<path to geckodriver.exe>` | Use a specific geckodriver instead of the auto-downloaded one |

---

## Test data

Login details and expected values are in `src/main/resources/data.properties`:

| Key | Meaning |
|---|---|
| `url` | Application URL |
| `username` / `password` | Account used by the positive login test |
| `expected_username` | Name shown in the user menu after login |
| `error_message` | Message expected by the negative login test |

If the account's display name changes, update `expected_username` to match.

---

## Run on Jenkins

Jenkins runs as the Windows `SYSTEM` service account, which has no desktop, so the run must be headless.

Normally no geckodriver path is needed, because Selenium Manager downloads the driver itself. Under
the `SYSTEM` account that download goes wrong: Selenium Manager is a 32-bit program, Windows silently
redirects its cache folder from `System32` to `SysWOW64`, and Selenium then looks for the driver in
the wrong place (`geckodriver located at ... but invalid`). Pointing at a geckodriver file directly
skips Selenium Manager. This applies to Jenkins only, not to a normal user account.

Use these Maven goals in the job:

```
test -Dsurefire.suiteXmlFiles=testng-local.xml -Dheadless=true -Dwebdriver.gecko.driver=C:\ProgramData\Jenkins\.jenkins\tools\geckodriver\geckodriver.exe
```

Or, if Selenoid is running on the same machine (no geckodriver needed at all, the browser and its
driver are inside the container):

```
test -Dsurefire.suiteXmlFiles=testng-selenoid.xml
```

---

## Troubleshooting

| Error | Cause | Fix |
|---|---|---|
| `UnknownHostException: selenium` | Old code with a hard-coded Grid host | Pull the latest code |
| `Connection refused` to `localhost:4444` | Selenoid is not running | `cm selenoid start --vnc` |
| `aerokube/selenoid:nc: not found` | Typed `-vnc` with one dash | Use `--vnc` |
| Session fails with image not found | Browser images were not downloaded | `cm selenoid start --vnc --force` |
| `geckodriver located at ...systemprofile... but invalid` | Jenkins running as `SYSTEM` | Add `-Dwebdriver.gecko.driver=...` (see Jenkins section) |
| `Your email, password, IP address or location did not match` in the positive test | The account in `data.properties` is no longer valid | Update `username` and `password` |
| Positive test fails on the user name assertion | Display name differs from `expected_username` | Update `expected_username` |
| `No tests matching pattern` | Wrong class or method name in `-Dtest` | Check the name, e.g. `LoginTest#testLoginPositive` |
