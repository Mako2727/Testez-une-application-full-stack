import { defineConfig } from 'cypress';

// ⚠ Typage explicite ici
const coverageTaskMod: any = require('@cypress/code-coverage/task');

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',
    setupNodeEvents(on, config) {
      const registerCoverage =
        typeof coverageTaskMod === 'function'
          ? coverageTaskMod
          : coverageTaskMod.default;

      if (typeof registerCoverage === 'function') {
        registerCoverage(on, config);
      } else {
        console.error('[code-coverage] Module inattendu :', coverageTaskMod);
      }

      return config;
    },
    specPattern: 'cypress/e2e/**/*.cy.{js,ts}',
    supportFile: 'cypress/support/e2e.ts',
  },
  env: {
    codeCoverage: {
      resetOnStart: false,
      exclude: [],
      coverageDirectory: 'coverage/cypress',  
    },
  },
  video: false,
  videosFolder: 'cypress/videos',
  screenshotsFolder: 'cypress/screenshots',
  fixturesFolder: 'cypress/fixtures',
});