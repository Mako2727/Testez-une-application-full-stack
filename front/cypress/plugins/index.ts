import * as codeCoverageTask from '@cypress/code-coverage/task';

export default (on, config) => {
  codeCoverageTask.default(on, config);
  return config;
};
