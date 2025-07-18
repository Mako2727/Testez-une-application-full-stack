import { defineConfig } from 'cypress';

// ⚠ Import en CommonJS pour éviter les soucis d'interop
// eslint-disable-next-line @typescript-eslint/no-var-requires
const coverageTaskMod = require('@cypress/code-coverage/task');

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',
    setupNodeEvents(on, config) {
      // Supporte les deux formes d'export (fn direct ou { default: fn })
      const registerCoverage =
        typeof coverageTaskMod === 'function'
          ? coverageTaskMod
          : coverageTaskMod.default;

      if (typeof registerCoverage === 'function') {
        registerCoverage(on, config);
      } else {
        // Log utile si jamais la lib change
        console.error(
          '[code-coverage] Module inattendu :',
          coverageTaskMod,
        );
      }

      return config;
    },
    specPattern: 'cypress/e2e/**/*.cy.{js,ts}',
    supportFile: 'cypress/support/e2e.ts',
  },

  video: false,
  videosFolder: 'cypress/videos',
  screenshotsFolder: 'cypress/screenshots',
  fixturesFolder: 'cypress/fixtures',
});

module.exports = defineConfig({
  // setupNodeEvents can be defined in either
  // the e2e or component configuration
  e2e: {
    setupNodeEvents(on, config) {
      require('@cypress/code-coverage/task')(on, config)
      // include any other plugin code...

      // It's IMPORTANT to return the config object
      // with any changed environment variables
      return config
    },
  },
})