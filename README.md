# The Log Punisher

Merge log files and apply filters in order to find the guilty and prosecuting the innocent.

![The Log Punisher](thelogpunisher.png)

## Usage

    java -jar thelogpunisher-x.y.z.jar [-hV] -c=FILE [-o=filename] [-f=KEY=VALUE]...

    -c, --configuration=FILE   Mandatory YAML configuration file with instructions
    -f, --filter=KEY=VALUE     Filter the input log(s) to extract only matching lines
    -h, --help                 Show help message and exit
    -o, --output=filename      Redirect output stream. If not set the stdout will be used. If output is specified according to its extension could be a Text file or CSV file.
    -V, --version              Print version information and exit

## Configuration file

Yaml configuration file contains `name` (name of the current investigation) and `logs`.

Each `logs` entry must declare it's `type`. The supported types of log files:

- `log`: File parsed by regular expression, for example Apache access, Apache Tomcat Access, Apache Tomcat output etc.
- `csv`: A comma separated values file is a is a delimited plain text file that contains a list of data, for example the export of a database table.

### Log configuration

- `file`: Relative path to the log, starting from the configuration file.
- `options`
  - `dateformat`: The pattern of the date and time used in the file.
  - `locale`: The locale whose date format symbols should be used to parse the date.
  - `pattern`: The regular expression to match the log row.
  - `matches`: The indexes of a capturing groups in this matcher's pattern of the global matching row pattern: date (mandatory), message (mandatory), etc.
  - `filters`: List of filtering rules: all lines which message doesn't match any of the filter regexp patterns will be ignored.
    - `pattern`: The filter regular expression.
    - `matches`: The indexes of a capturing groups in this matcher's pattern of the filter regexp.
  - `details`: In some cases there could be row with more information than we want to collect. Those rows can be specified setting detailed rules. Details, like `filters`, are regular expression that match the log message and get more details from it, but unlike `filters`, `details` do not prevent other rows to be matched. Filters and details are mutual exclusive.
    - `pattern`: The detail regular expression.
    - `matches`: The indexes of a capturing groups in this matcher's pattern of the detail regexp.

### CSV configuration

- `file`: Relative path to the CSV file, starting from the configuration file.
- `options`
  - `separator`: Delimiter between recolrd fields.
  - `quote`: Quote character for strings.
  - `dateformat`: The pattern describing the date and time format used.
  - `locale`: The locale whose date format symbols should be used to parse the date.
  - `matches`: The position of fields (date, ip, message, session, user).
